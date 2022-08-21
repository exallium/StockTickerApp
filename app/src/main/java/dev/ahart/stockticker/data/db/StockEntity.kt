package dev.ahart.stockticker.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock")
data class StockEntity(
  @PrimaryKey @ColumnInfo(name = "symbol") val symbol: String,
  @ColumnInfo(name = "description") val description: String
)