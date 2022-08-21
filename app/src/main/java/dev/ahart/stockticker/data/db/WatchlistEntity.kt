package dev.ahart.stockticker.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistEntity(
  @PrimaryKey @ColumnInfo(name = "symbol") val symbol: String
)