package dev.ahart.stockticker.list

import android.text.format.DateUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ahart.stockticker.data.FinnhubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class StockListScreenViewModel @Inject constructor(private val repository: FinnhubRepository) :
  ViewModel() {
  private val _uiState = MutableStateFlow(StockListState())
  val uiState: StateFlow<StockListState> = _uiState

  private var repositoryJob: Job? = null

  init {
    viewModelScope.launch {
      repository.fetchDefaultQuotesIfNoneExistLocally()
      repository.fetchDefaultQuotesIfThresholdExceeded(24, TimeUnit.HOURS)
      while (true) {
        updateUiState()
        delay(TimeUnit.MINUTES.toMillis(1))
      }
    }
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
        _uiState.value = StockListState(it)
      }
    }
  }

  private fun Float.formatAsPrice() = "$%.02f".format(this)
  private fun Long.formatAsDateTime() = DateUtils.getRelativeTimeSpanString(this).toString()
}