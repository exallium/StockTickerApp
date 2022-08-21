package dev.ahart.stockticker.data

import android.util.Log
import dev.ahart.stockticker.data.api.FinnhubQuoteJson
import dev.ahart.stockticker.data.api.FinnhubService
import dev.ahart.stockticker.data.db.QuoteEntity
import dev.ahart.stockticker.data.db.StockEntity
import dev.ahart.stockticker.data.db.StockTickerDatabase
import dev.ahart.stockticker.data.db.WatchlistEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class FinnhubRepository @Inject constructor(
  private val service: FinnhubService,
  private val database: StockTickerDatabase
) {
  companion object {
    private val TAG = FinnhubRepository::class.simpleName
  }

  suspend fun findMatchingSymbols(query: String): List<StockEntity> {
    return withContext(Dispatchers.IO) {
      if (query.isEmpty()) {
        emptyList()
      } else {
        database.stockDao().findStocks(query)
      }
    }
  }

  suspend fun addToWatchlist(symbol: String): FinnhubFetchResult {
    return withContext(Dispatchers.IO) {
      database.watchlistDao().addToWatchlist(WatchlistEntity(symbol))
      val quote = fetchQuote(symbol)
      if (quote != null) {
        database.quoteDao().insertAll(listOf(quote))
        FinnhubFetchResult.SUCCESS
      } else {
        FinnhubFetchResult.FAILURE
      }
    }
  }

  suspend fun fetchDefaultQuotes(): FinnhubFetchResult {
    return withContext(Dispatchers.IO) {
      var result = FinnhubFetchResult.SUCCESS
      val entities = Constants.FAANG.mapNotNull {
        val quote = fetchQuote(it)
        if (quote == null) {
          result = FinnhubFetchResult.FAILURE
        }
        quote
      }

      database.quoteDao().insertAll(entities)
      result
    }
  }

  private suspend fun fetchQuote(symbol: String): QuoteEntity? {
    val quote: FinnhubQuoteJson? = try {
      service.getQuote(symbol)
    } catch (e: UnknownHostException) {
      Log.w(TAG, "Could not download quote.", e)
      null
    }

    return if (quote != null) {
      QuoteEntity(
        symbol = symbol,
        currentPrice = quote.currentPrice,
        lowOfDay = quote.lowOfDay,
        highOfDay = quote.highOfDay,
        lastUpdated = System.currentTimeMillis()
      )
    } else {
      null
    }
  }

  fun getWatchlistQuotes(): Flow<List<QuoteEntity>> {
    return database.quoteDao().getWatchlistQuotes()
  }

  fun getFaangQuotes(): Flow<List<QuoteEntity>> {
    return database.quoteDao().getNonWatchlistQuotes(Constants.FAANG)
  }
}