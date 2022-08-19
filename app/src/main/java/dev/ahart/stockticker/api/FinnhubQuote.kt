package dev.ahart.stockticker.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FinnhubQuote(
  @Json(name = "c") val currentPrice: Float,
  @Json(name = "d") val change: Float,
  @Json(name = "dp") val percentChange: Float,
  @Json(name = "h") val highOfDay: Float,
  @Json(name = "l") val lowOfDay: Float,
  @Json(name = "o") val openOfDay: Float,
  @Json(name = "pc") val previousClose: Float
)