package dev.ahart.stockticker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {
  @Query("SELECT * FROM stock WHERE symbol LIKE (:query) || '%' ORDER BY symbol LIMIT 5")
  fun findStocks(query: String): List<StockEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertStocks(stocks: List<StockEntity>)
}