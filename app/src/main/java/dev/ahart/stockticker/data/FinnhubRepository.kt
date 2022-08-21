package dev.ahart.stockticker.data

import android.util.Log
import dev.ahart.stockticker.data.api.FinnhubQuoteJson
import dev.ahart.stockticker.data.api.FinnhubService
import dev.ahart.stockticker.data.db.QuoteEntity
import dev.ahart.stockticker.data.db.StockEntity
import dev.ahart.stockticker.data.db.StockTickerDatabase
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

  suspend fun fetchDefaultQuotes(): FinnhubFetchResult {
    return withContext(Dispatchers.IO) {
      var result = FinnhubFetchResult.SUCCESS
      val entities = Constants.FAANG.mapNotNull {
        val quote: FinnhubQuoteJson? = try {
          service.getQuote(it)
        } catch (e: UnknownHostException) {
          Log.w(TAG, "Could not download quote.", e)
          result = FinnhubFetchResult.FAILURE
          null
        }

        if (quote != null) {
          QuoteEntity(
            symbol = it,
            currentPrice = quote.currentPrice,
            lowOfDay = quote.lowOfDay,
            highOfDay = quote.highOfDay,
            lastUpdated = System.currentTimeMillis()
          )
        } else {
          null
        }
      }

      database.quoteDao().insertAll(entities)
      result
    }
  }

  fun getFaangQuotes(): Flow<List<QuoteEntity>> {
    return database.quoteDao().getQuotes(Constants.FAANG)
  }
}