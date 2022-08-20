package dev.ahart.stockticker.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ahart.stockticker.data.api.FinnhubService
import dev.ahart.stockticker.data.db.FinnhubDatabase

@Module
@InstallIn(SingletonComponent::class)
class FinnhubModule {
  @Provides
  fun bindFinnhubService(): FinnhubService {
    return FinnhubService.create()
  }

  @Provides
  fun bindFinnhubDatabase(@ApplicationContext context: Context): FinnhubDatabase {
    return Room.databaseBuilder(
      context,
      FinnhubDatabase::class.java,
      "stock-ticker"
    ).build()
  }
}