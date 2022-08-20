package dev.ahart.stockticker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FinnhubQuoteEntity::class], version = 1)
abstract class FinnhubDatabase : RoomDatabase() {
  abstract fun quoteDao(): FinnhubQuoteDao
}