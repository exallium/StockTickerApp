package dev.ahart.stockticker.data

import dev.ahart.stockticker.data.api.FinnhubService
import dev.ahart.stockticker.data.db.FinnhubDatabase
import dev.ahart.stockticker.data.db.FinnhubQuoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FinnhubRepository @Inject constructor(
  private val database: FinnhubDatabase,
  private val service: FinnhubService)
{
  suspend fun fetchDefaultQuotesIfNoneExistLocally() {
    withContext(Dispatchers.IO) {
      val count = database.quoteDao().getCount(FAANG)
      if (count == 0) {
        fetchDefaultQuotes()
      }
    }
  }

  suspend fun fetchDefaultQuotesIfThresholdExceeded(threshold: Long, timeUnit: TimeUnit) {
    withContext(Dispatchers.IO) {
      val timeAgo = System.currentTimeMillis() - timeUnit.toMillis(threshold)
      if (database.quoteDao().getOldestLastUpdatedBeforeThreshold(FAANG, timeAgo) != null) {
        fetchDefaultQuotes()
      }
    }
  }

  suspend fun fetchDefaultQuotes() {
    withContext(Dispatchers.IO) {
      val entities = FAANG.map {
        val quote = service.getQuote(it)
        FinnhubQuoteEntity(
          symbol = it,
          currentPrice = quote.currentPrice,
          lowOfDay = quote.lowOfDay,
          highOfDay = quote.highOfDay,
          lastUpdated = System.currentTimeMillis()
        )
      }

      database.quoteDao().insertAll(entities)
    }
  }

  fun getDefaultQuotes(): Flow<List<FinnhubQuoteEntity>> {
    return database.quoteDao().getQuotes(FAANG)
  }

  companion object {
    private val FAANG = listOf("META", "AAPL", "AMZN", "MSFT", "NFLX", "GOOGL")
  }
}