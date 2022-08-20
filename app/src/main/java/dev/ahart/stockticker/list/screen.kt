package dev.ahart.stockticker.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.ahart.stockticker.R

@Preview
@Composable
fun StockListScreenPreview() {
  StockListScreen(
    StockListState(
      listOf(
        StockQuote(
          "ASDF",
          "$1.00",
          "$2.00",
          "$0.50",
          "23 minutes ago"
        )
      )
    )
  )
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
private fun StockQuoteCard(stockQuote: StockQuote) {
  Card(
    modifier = Modifier
      .padding(horizontal = 16.dp, vertical = 8.dp)
      .fillMaxWidth()
  ) {
    Row(modifier = Modifier.padding(16.dp)) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = stockQuote.symbol,
          style = MaterialTheme.typography.headlineMedium
        )
        Text(
          text = stringResource(id = R.string.last_updated),
          style = MaterialTheme.typography.labelMedium
        )
        Text(
          text = stockQuote.lastUpdated,
          style = MaterialTheme.typography.bodySmall
        )
      }

      StockQuotePrices(stockQuote = stockQuote)
    }
  }
}

@Composable
private fun StockQuotePrices(stockQuote: StockQuote) {
  Column {
    Text(text = stringResource(id = R.string.current_price))
    Text(text = stringResource(id = R.string.low_of_the_day), color = MaterialTheme.colorScheme.error)
    Text(text = stringResource(id = R.string.high_of_the_day))
  }
  Column(modifier = Modifier.padding(start = 16.dp)) {
    Text(text = stockQuote.currentPrice)
    Text(text = stockQuote.lowOfDay, color = MaterialTheme.colorScheme.error)
    Text(text = stockQuote.highOfDay)
  }
}