package dev.ahart.stockticker.list

/**
 * State for the StockListScreen
 *
 * @param isRefreshing Whether or not we're currently downloading data
 * @param quotes The stock quotes to display.
 */
data class StockListState(
  val isRefreshing: Boolean = false,
  val quotes: List<StockQuote> = emptyList()
)