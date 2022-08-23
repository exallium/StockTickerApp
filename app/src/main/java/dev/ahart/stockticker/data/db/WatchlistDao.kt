package dev.ahart.stockticker.data.db

import androidx.room.*

@Dao
interface WatchlistDao {
  @Query("SELECT symbol FROM watchlist")
  fun getWatchlist(): List<WatchlistEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun addToWatchlist(watchlistEntity: WatchlistEntity)

  @Delete
  fun removeFromWatchlist(watchlistEntity: WatchlistEntity)
}