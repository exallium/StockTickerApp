package dev.ahart.stockticker.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FinnhubSymbolJson(
  @Json(name="currency") val currency: String,
  @Json(name="description") val description: String,
  @Json(name="displaySymbol") val displaySymbol: String,
  @Json(name="figi") val figi: String,
  @Json(name="mic") val mic: String,
  @Json(name="symbol") val symbol: String,
  @Json(name="type") val type: String
)