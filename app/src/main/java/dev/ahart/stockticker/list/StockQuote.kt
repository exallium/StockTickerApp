package dev.ahart.stockticker.list

/**
 * Presentation data for a given stock quote.
 */
data class StockQuote(
  val symbol: String,
  val currentPrice: String,
  val highOfDay: String,
  val lowOfDay: String,
  val lastUpdated: String
)