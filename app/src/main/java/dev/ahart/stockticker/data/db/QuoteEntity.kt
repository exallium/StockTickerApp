package dev.ahart.stockticker.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A stock quote. Includes:
 * - The symbol
 * - The price at last access time
 * - The low price of the day as of last access time
 * - The high price of the day as of last access time
 * - When this record was last updated
 */
@Entity(tableName = "quote")
data class QuoteEntity(
  @PrimaryKey @ColumnInfo(name = "symbol") val symbol: String,
  @ColumnInfo(name = "current_price") val currentPrice: Float,
  @ColumnInfo(name = "low_of_day") val lowOfDay: Float,
  @ColumnInfo(name = "high_of_day") val highOfDay: Float,
  @ColumnInfo(name = "last_updated") val lastUpdated: Long
)