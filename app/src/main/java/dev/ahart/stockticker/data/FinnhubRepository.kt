package dev.ahart.stockticker.data

import android.util.Log
import dev.ahart.stockticker.data.api.FinnhubQuoteJson
import dev.ahart.stockticker.data.api.FinnhubService
import dev.ahart.stockticker.data.db.*
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

  suspend fun removeFromWatchlist(symbol: String) {
    return withContext(Dispatchers.IO) {
      val watchlistEntity = WatchlistEntity(symbol)
      database.watchlistDao().removeFromWatchlist(watchlistEntity)
      if (symbol !in Constants.FAANG) {
        database.quoteDao().delete(watchlistEntity)
      }
    }
  }

  suspend fun fetchQuotes(): FinnhubFetchResult {
    return withContext(Dispatchers.IO) {
      val defaultResult = fetchDefaultQuotes()
      val watchlistResult = fetchWatchlistQuotes()

      if (defaultResult.isFailure() || watchlistResult.isFailure()) {
        FinnhubFetchResult.FAILURE
      } else {
        FinnhubFetchResult.SUCCESS
      }
    }
  }

  fun getWatchlistQuotes(): Flow<List<QuoteEntity>> {
    return database.quoteDao().getWatchlistQuotes()
  }

  fun getFaangQuotes(): Flow<List<QuoteEntity>> {
    return database.quoteDao().getNonWatchlistQuotes(Constants.FAANG)
  }

  private suspend fun fetchDefaultQuotes(): FinnhubFetchResult {
    return withContext(Dispatchers.IO) {
      var result = FinnhubFetchResult.SUCCESS
      val entities = Constants.FAANG.mapNotNull {
        val quote = fetchQuote(it)
        if (quote == null) {
          Log.w(TAG, "Found a null quote in defaults, reporting failure.")
          result = FinnhubFetchResult.FAILURE
        }
        quote
      }

      database.quoteDao().insertAll(entities)
      result
    }
  }

  private suspend fun fetchWatchlistQuotes(): FinnhubFetchResult {
    return withContext(Dispatchers.IO) {
      var result = FinnhubFetchResult.SUCCESS
      val entities = database.watchlistDao().getWatchlist().map { it.symbol }.mapNotNull {
        val quote = fetchQuote(it)
        if (quote == null) {
          Log.w(TAG, "Found a null quote in watchlist, reporting failure.")
          result = FinnhubFetchResult.FAILURE
        }
        quote
      }

      database.quoteDao().insertAll(entities)
      result
    }
  }

  private suspend fun fetchQuote(symbol: String): QuoteEntity? {
    return try {
      service.getQuote(symbol).let { quote ->
        QuoteEntity(
          symbol = symbol,
          currentPrice = quote.currentPrice,
          lowOfDay = quote.lowOfDay,
          highOfDay = quote.highOfDay,
          lastUpdated = System.currentTimeMillis()
        )
      }
    } catch (e: UnknownHostException) {
      Log.w(TAG, "Could not download quote.", e)
      null
    }
  }
}