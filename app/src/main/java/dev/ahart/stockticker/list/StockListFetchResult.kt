package dev.ahart.stockticker.list

enum class StockListFetchResult {
  SUCCESS,
  NO_NETWORK_FAILURE,
  ALREADY_FRESH;

  fun isError() = this == NO_NETWORK_FAILURE
}