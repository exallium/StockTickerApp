package dev.ahart.stockticker.list

import android.text.format.DateUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class StockListScreenViewModel @Inject constructor(private val repository: StockListScreenRepository) :
  ViewModel() {
  private val _uiState = MutableStateFlow(StockListState())
  val uiState: StateFlow<StockListState> = _uiState

  private var repositoryJob: Job? = null

  init {
    viewModelScope.launch {
      when {
        repository.checkNoDefaultQuotesExistLocally() -> {
          performRefresh()
        }
        repository.checkDefaultQuotesThresholdExceeded(24, TimeUnit.HOURS) -> {
          performRefresh()
        }
      }

      while (true) {
        updateUiState()
        delay(TimeUnit.MINUTES.toMillis(1))
      }
    }
  }

  fun refresh() {
    viewModelScope.launch {
      performRefresh()
    }
  }

  private suspend fun performRefresh() {
    _uiState.value = _uiState.value.copy(isRefreshing = true)

    val fetchResult = repository.fetchDefaultQuotes()

    _uiState.value = _uiState.value.copy(
      isRefreshing = false,
      networkErrorKey = if (fetchResult.isError()) System.currentTimeMillis() else null
    )

    updateUiState()
  }

  private fun updateUiState() {
    repositoryJob?.cancel()
    repositoryJob = viewModelScope.launch(context = Dispatchers.IO) {
      repository.getDefaultQuotes().map { entities ->
        entities.map { entity ->
          StockQuote(
            entity.symbol,
            entity.currentPrice.formatAsPrice(),
            entity.highOfDay.formatAsPrice(),
            entity.lowOfDay.formatAsPrice(),
            entity.lastUpdated.formatAsDateTime()
          )
        }
      }.cancellable().collectLatest {
        _uiState.value = _uiState.value.copy(
          quotes = it,
          isPerformingInitialLoad = false
        )
      }
    }
  }

  /**
   * Formats a float as a String with 2 decimal places.
   */
  private fun Float.formatAsPrice() = "$%.02f".format(this)

  /**
   * Formats a long as a relative timestamp.
   */
  private fun Long.formatAsDateTime() = DateUtils.getRelativeTimeSpanString(this).toString()
}