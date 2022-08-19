package dev.ahart.stockticker.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockListScreenViewModel @Inject constructor(repository: StockListScreenRepository) : ViewModel() {
  private val _uiState = MutableStateFlow(StockListState())
  val uiState: StateFlow<StockListState> = _uiState

  init {
    viewModelScope.launch {
      _uiState.value = StockListState(
        quotes = repository.getQuotesForFAANG()
      )
    }
  }
}