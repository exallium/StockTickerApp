package dev.ahart.stockticker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface WatchlistDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun addToWatchlist(watchlistEntity: WatchlistEntity)
}