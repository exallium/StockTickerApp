package dev.ahart.stockticker.list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StockListScreenViewModel : ViewModel() {
  private val _uiState = MutableStateFlow(StockListState())
  val uiState: StateFlow<StockListState> = _uiState
}