package dev.ahart.stockticker.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FinnhubModule {
  @Provides
  fun bindFinnhubService(): FinnhubService {
    return FinnhubService.create()
  }
}