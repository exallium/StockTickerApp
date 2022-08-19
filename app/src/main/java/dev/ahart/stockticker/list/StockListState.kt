package dev.ahart.stockticker.list

data class StockListState(
  val quotes: List<StockQuote> = emptyList()
)