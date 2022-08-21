package dev.ahart.stockticker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WatchlistDao {
  @Query("SELECT symbol FROM watchlist")
  fun getWatchlist(): List<WatchlistEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun addToWatchlist(watchlistEntity: WatchlistEntity)
}