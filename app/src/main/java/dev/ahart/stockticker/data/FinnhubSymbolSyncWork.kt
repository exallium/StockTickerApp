package dev.ahart.stockticker.data

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.ahart.stockticker.Settings
import dev.ahart.stockticker.data.api.FinnhubService
import dev.ahart.stockticker.data.db.StockEntity
import dev.ahart.stockticker.data.db.StockTickerDatabase
import dev.ahart.stockticker.settingsDataStore
import kotlinx.coroutines.flow.first
import java.net.UnknownHostException

/**
 * WorkManager work which will download the symbol
 * information from Finnhub and sync it with the local database.
 */
@HiltWorker
class FinnhubSymbolSyncWork @AssistedInject constructor(
  @Assisted appContext: Context,
  @Assisted workerParams: WorkerParameters,
  private val service: FinnhubService,
  private val database: StockTickerDatabase
) : CoroutineWorker(appContext, workerParams) {

  companion object {
    private val TAG = FinnhubSymbolSyncWork::class.simpleName
  }

  override suspend fun doWork(): Result {
    val settings: Settings = applicationContext.settingsDataStore.data.first()
    if (settings.symbolsFetchState == Settings.SymbolsFetchState.COMPLETE) {
      Log.i(TAG, "Already fetched symbols, finishing.")
      return Result.success()
    }

    applicationContext.settingsDataStore.updateData {
      it.toBuilder().setSymbolsFetchState(Settings.SymbolsFetchState.IN_PROGRESS).build()
    }

    val stockData = try {
      service.getSymbols("US")
        .map { StockEntity(symbol = it.symbol, description = it.description) }
    } catch (exception: UnknownHostException) {
      Log.i(TAG, "Failed to download stock data due to network issues.")
      applicationContext.settingsDataStore.updateData {
        it.toBuilder().setSymbolsFetchState(Settings.SymbolsFetchState.INIT).build()
      }
      return Result.retry()
    }

    Log.i(TAG, "Downloaded ${stockData.size} stocks, inserting into database.")
    database.stockDao().insertStocks(stockData)
    applicationContext.settingsDataStore.updateData {
      it.toBuilder().setSymbolsFetchState(Settings.SymbolsFetchState.COMPLETE).build()
    }
    return Result.success()
  }
}