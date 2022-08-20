package dev.ahart.stockticker.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FinnhubQuoteDao {
  @Query("SELECT * FROM quote WHERE symbol IN (:symbols)")
  fun getQuotes(symbols: List<String>): Flow<List<FinnhubQuoteEntity>>

  @Query("SELECT * FROM quote WHERE symbol IN (:symbols) AND last_updated < :threshold  ORDER BY last_updated ASC LIMIT 1")
  fun getOldestLastUpdatedBeforeThreshold(symbols: List<String>, threshold: Long): FinnhubQuoteEntity?

  @Query("SELECT COUNT(*) FROM quote WHERE symbol IN (:symbols)")
  fun getCount(symbols: List<String>): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(quotes: List<FinnhubQuoteEntity>)
}