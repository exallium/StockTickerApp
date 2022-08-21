package dev.ahart.stockticker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Database entrypoint for quotes.
 */
@Database(entities = [QuoteEntity::class,StockEntity::class,WatchlistEntity::class], version = 1)
abstract class StockTickerDatabase : RoomDatabase() {
  abstract fun quoteDao(): QuoteDao
  abstract fun stockDao(): StockDao
  abstract fun watchlistDao(): WatchlistDao
}