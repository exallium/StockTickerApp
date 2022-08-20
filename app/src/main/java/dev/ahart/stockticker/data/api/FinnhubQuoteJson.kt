package dev.ahart.stockticker.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Note:
 *
 * I know that I should never be using Floats in financial
 * situations, HOWEVER, this app does not support any kind
 * of actual transactions, and it is expressly for
 * demonstrative purposes. Also changing these to strings
 * seems to still result in more than 2 decimals but that
 * could be a code generation issue.
 */
@JsonClass(generateAdapter = true)
data class FinnhubQuoteJson(
  @Json(name = "c") val currentPrice: Float,
  @Json(name = "d") val change: Float,
  @Json(name = "dp") val percentChange: Float,
  @Json(name = "h") val highOfDay: Float,
  @Json(name = "l") val lowOfDay: Float,
  @Json(name = "o") val openOfDay: Float,
  @Json(name = "pc") val previousClose: Float
)