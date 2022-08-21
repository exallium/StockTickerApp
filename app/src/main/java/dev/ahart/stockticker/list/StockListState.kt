package dev.ahart.stockticker.list

/**
 * State for the StockListScreen
 *
 * @param isRefreshing Whether or not we're currently downloading data
 * @param faangQuotes The stock quotes to display.
 */
data class StockListState(
  val isRefreshing: Boolean = false,
  val networkErrorKey: Long? = null,
  val watchlistQuotes: List<StockQuote> = emptyList(),
  val faangQuotes: List<StockQuote> = emptyList(),
  private val isPerformingInitialLoad: Boolean = true,
  val symbolSearchState: SymbolSearchState = SymbolSearchState()
) {
  val displayNetworkErrorCard = !isPerformingInitialLoad &&
      !isRefreshing &&
      faangQuotes.isEmpty() &&
      watchlistQuotes.isEmpty()

  data class SymbolSearchState(
    val query: String = "",
    val suggestions: List<SymbolSearchSuggestion> = emptyList()
  )

  data class SymbolSearchSuggestion(
    val symbol: String,
    val description: String
  )
}