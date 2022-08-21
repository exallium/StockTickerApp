package dev.ahart.stockticker.list

/**
 * State for the StockListScreen
 *
 * @param isRefreshing Whether or not we're currently downloading data
 * @param quotes The stock quotes to display.
 */
data class StockListState(
  val isRefreshing: Boolean = false,
  val networkErrorKey: Long? = null,
  val quotes: List<StockQuote> = emptyList(),
  private val isPerformingInitialLoad: Boolean = true,
  val symbolSearchState: SymbolSearchState = SymbolSearchState()
) {
  val displayNetworkErrorCard = !isPerformingInitialLoad && !isRefreshing && quotes.isEmpty()

  data class SymbolSearchState(
    val query: String = "",
    val suggestions: List<SymbolSearchSuggestion> = emptyList()
  )

  data class SymbolSearchSuggestion(
    val symbol: String,
    val description: String
  )
}