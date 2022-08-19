package dev.ahart.stockticker.list

import dev.ahart.stockticker.api.FinnhubService
import javax.inject.Inject

class StockListScreenRepository @Inject constructor(val service: FinnhubService) {

  suspend fun getQuotesForFAANG(): List<StockQuote> {
    return FAANG.map {
      val finnhubQuote = service.getQuote(it)
      StockQuote(it, finnhubQuote.currentPrice, finnhubQuote.highOfDay, finnhubQuote.lowOfDay)
    }
  }

  companion object {
    private val FAANG = listOf("META", "AAPL", "AMZN", "MSFT", "NFLX", "GOOGL")
  }
}