package dev.ahart.stockticker.list

data class StockQuote(
  val symbol: String,
  val currentPrice: Float,
  val highOfDay: Float,
  val lowOfDay: Float
)