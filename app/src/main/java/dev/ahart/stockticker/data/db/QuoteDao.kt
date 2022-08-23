package dev.ahart.stockticker.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Simple DAO for interacting with Quotes
 */
@Dao
interface QuoteDao {
  @Query("SELECT * FROM quote WHERE symbol IN (SELECT symbol FROM watchlist)")
  fun getWatchlistQuotes(): Flow<List<QuoteEntity>>

  @Query("SELECT * FROM quote WHERE symbol IN (:symbols) AND symbol NOT IN (SELECT symbol from watchlist)")
  fun getNonWatchlistQuotes(symbols: List<String>): Flow<List<QuoteEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(quotes: List<QuoteEntity>)

  @Delete(entity = QuoteEntity::class)
  fun delete(watchlistEntity: WatchlistEntity)
}