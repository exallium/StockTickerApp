package dev.ahart.stockticker.list

import dev.ahart.stockticker.api.FinnhubQuote
import dev.ahart.stockticker.api.FinnhubService
import javax.inject.Inject

class StockListScreenRepository @Inject constructor(val service: FinnhubService) {

  suspend fun getQuotesForFAANG(): List<Pair<String, FinnhubQuote>> {
    return FAANG.map {
      val finnhubQuote = service.getQuote(it)
      Pair(it, finnhubQuote)
    }
  }

  companion object {
    private val FAANG = listOf("META", "AAPL", "AMZN", "MSFT", "NFLX", "GOOGL")
  }
}