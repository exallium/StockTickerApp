package dev.ahart.stockticker.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quote")
data class FinnhubQuoteEntity(
  @PrimaryKey @ColumnInfo(name = "symbol") val symbol: String,
  @ColumnInfo(name = "current_price") val currentPrice: Float,
  @ColumnInfo(name = "low_of_day") val lowOfDay: Float,
  @ColumnInfo(name = "high_of_day") val highOfDay: Float,
  @ColumnInfo(name = "last_updated") val lastUpdated: Long
)