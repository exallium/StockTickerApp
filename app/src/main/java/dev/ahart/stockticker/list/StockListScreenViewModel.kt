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

  private var watchlistJob: Job? = null
  private var faangJob: Job? = null
  private var searchJob: Job? = null

  init {
    viewModelScope.launch {
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

  fun onSymbolSearchQueryChanged(query: String) {
    _uiState.value = _uiState.value.copy(
      symbolSearchState = _uiState.value.symbolSearchState.copy(query = query)
    )

    searchJob?.cancel()
    searchJob = viewModelScope.launch {
      _uiState.value = _uiState.value.copy(
        symbolSearchState = _uiState.value.symbolSearchState.copy(
          suggestions = repository.findMatchingSymbols(query).map {
            StockListState.SymbolSearchSuggestion(it.symbol, it.description)
          }
        )
      )
    }
  }

  fun onSymbolSearchSuggestionSelected(symbolSearchSuggestion: StockListState.SymbolSearchSuggestion) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(
        symbolSearchState = StockListState.SymbolSearchState()
      )

      val addResult = repository.addToWatchlist(symbolSearchSuggestion.symbol)

      _uiState.value = _uiState.value.copy(
        networkErrorKey = if (addResult.isFailure()) System.currentTimeMillis() else _uiState.value.networkErrorKey
      )
    }
  }

  private suspend fun performRefresh() {
    _uiState.value = _uiState.value.copy(isRefreshing = true)

    val fetchResult = repository.fetchDefaultQuotes()

    _uiState.value = _uiState.value.copy(
      isRefreshing = false,
      networkErrorKey = if (fetchResult.isFailure()) System.currentTimeMillis() else null
    )

    updateUiState()
  }

  private fun updateUiState() {
    watchlistJob?.cancel()
    watchlistJob = viewModelScope.launch(context = Dispatchers.IO) {
      repository.getWatchlistQuotes().map { entities ->
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
          watchlistQuotes = it
        )
      }
    }

    faangJob?.cancel()
    faangJob = viewModelScope.launch(context = Dispatchers.IO) {
      repository.getFaangQuotes().map { entities ->
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
          faangQuotes = it,
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