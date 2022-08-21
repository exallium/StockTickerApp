package dev.ahart.stockticker.data

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FinnhubFaangQuotesSyncWork @AssistedInject constructor(
  @Assisted applicationContext: Context,
  @Assisted workerParameters: WorkerParameters,
  private val repository: FinnhubRepository
) : CoroutineWorker(applicationContext, workerParameters) {
  companion object {
    val TAG = FinnhubFaangQuotesSyncWork::class.simpleName
  }

  override suspend fun doWork(): Result {
    Log.i(TAG, "Downloading latest FAANG quotes...")
    return when (repository.fetchDefaultQuotes()) {
      FinnhubFetchResult.SUCCESS -> Result.success()
      FinnhubFetchResult.FAILURE -> Result.retry()
    }
  }
}