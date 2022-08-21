package dev.ahart.stockticker

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import dagger.hilt.android.HiltAndroidApp
import dev.ahart.stockticker.data.FinnhubQuotesSyncWork
import dev.ahart.stockticker.data.FinnhubSymbolSyncWork
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {

  @Inject
  lateinit var workerFactory: HiltWorkerFactory

  override fun onCreate() {
    super.onCreate()

    val stockWorker = OneTimeWorkRequestBuilder<FinnhubSymbolSyncWork>()
      .setConstraints(Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
      )
      .build()

    val quotesWorker = PeriodicWorkRequestBuilder<FinnhubQuotesSyncWork>(24, TimeUnit.HOURS)
      .setConstraints(Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
      )
      .build()

    WorkManager
      .getInstance(this)
      .enqueue(listOf(stockWorker, quotesWorker))
  }

  override fun getWorkManagerConfiguration(): Configuration {
    return Configuration.Builder()
      .setWorkerFactory(workerFactory)
      .build()
  }
}