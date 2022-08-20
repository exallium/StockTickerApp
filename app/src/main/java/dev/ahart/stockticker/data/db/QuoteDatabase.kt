package dev.ahart.stockticker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Database entrypoint for quotes.
 */
@Database(entities = [QuoteEntity::class], version = 1)
abstract class QuoteDatabase : RoomDatabase() {
  abstract fun quoteDao(): QuoteDao
}