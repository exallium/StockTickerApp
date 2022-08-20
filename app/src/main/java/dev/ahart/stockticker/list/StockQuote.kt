package dev.ahart.stockticker.list

data class StockQuote(
  val symbol: String,
  val currentPrice: String,
  val highOfDay: String,
  val lowOfDay: String,
  val lastUpdated: String
)