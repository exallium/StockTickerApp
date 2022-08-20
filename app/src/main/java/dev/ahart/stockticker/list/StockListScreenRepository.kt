package dev.ahart.stockticker.list

import android.util.Log
import dev.ahart.stockticker.data.api.FinnhubQuoteJson
import dev.ahart.stockticker.data.api.FinnhubService
import dev.ahart.stockticker.data.db.QuoteDatabase
import dev.ahart.stockticker.data.db.QuoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StockListScreenRepository @Inject constructor(
  private val database: QuoteDatabase,
  private val service: FinnhubService)
{
  suspend fun checkNoDefaultQuotesExistLocally(): Boolean {
    return withContext(Dispatchers.IO) {
      database.quoteDao().getCount(FAANG) == 0
    }
  }

  suspend fun checkDefaultQuotesThresholdExceeded(threshold: Long, timeUnit: TimeUnit): Boolean {
    return withContext(Dispatchers.IO) {
      val timeAgo = System.currentTimeMillis() - timeUnit.toMillis(threshold)
      database.quoteDao().getOldestLastUpdatedBeforeThreshold(FAANG, timeAgo) != null
    }
  }

  /**
   * Fetches the "default" quotes (in this case, a FAANG selection)
   * and returns a boolean based on whether we were able to download
   * them all successfully.
   */
  suspend fun fetchDefaultQuotes(): StockListFetchResult {
    return withContext(Dispatchers.IO) {
      var result: StockListFetchResult = StockListFetchResult.SUCCESS
      val entities = FAANG.mapNotNull {
        val quote: FinnhubQuoteJson? = try {
          service.getQuote(it)
        } catch (e: UnknownHostException) {
          Log.w(TAG, "Could not download quote.", e)
          result = StockListFetchResult.NO_NETWORK_FAILURE
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

  fun getDefaultQuotes(): Flow<List<QuoteEntity>> {
    return database.quoteDao().getQuotes(FAANG)
  }

  companion object {
    private val TAG = StockListScreenRepository::class.simpleName
    private val FAANG = listOf("META", "AAPL", "AMZN", "MSFT", "NFLX", "GOOGL")
  }

}