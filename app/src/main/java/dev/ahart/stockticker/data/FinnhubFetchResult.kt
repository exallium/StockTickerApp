package dev.ahart.stockticker.data

enum class FinnhubFetchResult {
  SUCCESS,
  FAILURE;

  fun isFailure() = this == FAILURE
}