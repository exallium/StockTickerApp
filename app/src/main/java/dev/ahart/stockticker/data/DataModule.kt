package dev.ahart.stockticker.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ahart.stockticker.data.api.FinnhubService
import dev.ahart.stockticker.data.db.QuoteDatabase

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
  @Provides
  fun provideFinnhubService(): FinnhubService {
    return FinnhubService.create()
  }

  @Provides
  fun provideDatabase(@ApplicationContext context: Context): QuoteDatabase {
    return Room.databaseBuilder(
      context,
      QuoteDatabase::class.java,
      "stock-ticker"
    ).build()
  }
}