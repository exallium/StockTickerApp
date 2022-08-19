package dev.ahart.stockticker.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun StockListScreenPreview() {
  StockListScreen(StockListState(
    listOf(StockQuote("ASDF", 0f, 0f, 0f))
  ))
}

@Composable
fun StockListScreen(stockListState: StockListState) {
  LazyColumn(content = {
    items(stockListState.quotes) { quote ->
      StockQuoteCard(stockQuote = quote)
    }
  })
}

@Composable
fun StockQuoteCard(stockQuote: StockQuote) {
  Card(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
    Text(
      modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 0.dp),
      text = stockQuote.symbol,
      style = MaterialTheme.typography.headlineMedium
    )
    Text(
      modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp),
      text = "${stockQuote.currentPrice} : ${stockQuote.lowOfDay} : ${stockQuote.highOfDay}")
  }
}