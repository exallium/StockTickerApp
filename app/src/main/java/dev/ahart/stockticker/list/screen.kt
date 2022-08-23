package dev.ahart.stockticker.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.ahart.stockticker.R
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Preview
@Composable
fun StockListScreenPreview() {
  StockListScreen(
    StockListState(
      faangQuotes = listOf(
        StockQuote(
          "ASDF",
          "$1.00",
          "$2.00",
          "$0.50",
          "23 minutes ago"
        )
      )
    ),
    {}, {}, {}, {})
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun StockListScreen(
  stockListState: StockListState,
  onSwipeToRefresh: () -> Unit,
  onSymbolSearchQueryChanged: (String) -> Unit,
  onSymbolSearchSuggestionClicked: (StockListState.SymbolSearchSuggestion) -> Unit,
  onWatchlistRowSwiped: (StockQuote) -> Unit
) {
  val snackbarHostState = remember { SnackbarHostState() }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    topBar = {
      SymbolSearch(
        stockListState.symbolSearchState,
        onSymbolSearchQueryChanged,
        onSymbolSearchSuggestionClicked
      )
    }
  ) { paddingValues ->
    SwipeRefresh(
      modifier = Modifier.padding(paddingValues),
      state = rememberSwipeRefreshState(isRefreshing = stockListState.isRefreshing),
      onRefresh = onSwipeToRefresh
    ) {
      if (stockListState.displayNetworkErrorCard) {
        FailedToDownloadQuotesCard(onSwipeToRefresh)
      } else {
        LazyColumn(content = {
          if (stockListState.watchlistQuotes.isNotEmpty()) {
            item(key = "watchlist-header") {
              Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(id = R.string.watchlist),
                style = MaterialTheme.typography.titleLarge
              )
            }
            items(
              items = stockListState.watchlistQuotes,
              key = { it.symbol }
            ) { quote ->
              WatchlistQuoteCard(
                modifier = Modifier.animateItemPlacement(),
                stockQuote = quote, onWatchlistRowSwiped
              )
            }
          }

          item(key = "faang-header") {
            Text(
              modifier = Modifier.padding(16.dp),
              text = stringResource(id = R.string.faang_stock_quotes),
              style = MaterialTheme.typography.titleLarge
            )
          }
          items(
            items = stockListState.faangQuotes,
            key = { it.symbol }
          ) { quote ->
            StockQuoteCard(
              modifier = Modifier.animateItemPlacement(),
              stockQuote = quote
            )
          }
        })
      }
    }
  }

  if (stockListState.faangQuotes.isNotEmpty()) {
    NetworkErrorSnackBarEffect(snackbarHostState, stockListState.networkErrorKey)
  }
}

@Composable
private fun NetworkErrorSnackBarEffect(
  snackbarHostState: SnackbarHostState,
  networkErrorKey: Long?
) {
  if (networkErrorKey != null) {
    val message = stringResource(id = R.string.failed_to_download_quotes)
    LaunchedEffect(networkErrorKey) {
      snackbarHostState.showSnackbar(
        message,
        withDismissAction = true,
        duration = SnackbarDuration.Indefinite
      )
    }
  }
}

@Preview
@Composable
private fun FailedToDownloadQuotesCardPreview() {
  FailedToDownloadQuotesCard {}
}

@Composable
private fun FailedToDownloadQuotesCard(onClickToRefresh: () -> Unit) {
  Card(
    modifier = Modifier
      .padding(16.dp)
      .fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .padding(16.dp)
        .align(Alignment.CenterHorizontally),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = stringResource(id = R.string.failed_to_download_quotes),
        style = MaterialTheme.typography.titleLarge
      )
      Text(text = stringResource(id = R.string.please_check_your_network_connection))
      FilledTonalButton(
        onClick = onClickToRefresh
      ) {
        Text(stringResource(id = R.string.retry))
      }
    }
  }
}

@Composable
private fun WatchlistQuoteCard(
  modifier: Modifier,
  stockQuote: StockQuote,
  onSwipe: (StockQuote) -> Unit
) {
  val startAction = SwipeAction(
    onSwipe = { onSwipe(stockQuote) },
    icon = {
      Icon(
        Icons.Default.Delete,
        contentDescription = stringResource(id = R.string.remove_from_watchlist)
      )
    },
    background = MaterialTheme.colorScheme.error
  )

  SwipeableActionsBox(
    modifier = modifier,
    startActions = listOf(startAction),
    backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.background
  ) {
    StockQuoteCard(stockQuote = stockQuote)
  }
}

@Composable
private fun StockQuoteCard(modifier: Modifier = Modifier, stockQuote: StockQuote) {
  Card(
    modifier = modifier
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
    Text(text = stringResource(id = R.string.latest_price))
    Text(
      text = stringResource(id = R.string.low_of_the_day),
      color = MaterialTheme.colorScheme.error
    )
    Text(text = stringResource(id = R.string.high_of_the_day))
  }
  Column(modifier = Modifier.padding(start = 16.dp)) {
    Text(text = stockQuote.currentPrice)
    Text(text = stockQuote.lowOfDay, color = MaterialTheme.colorScheme.error)
    Text(text = stockQuote.highOfDay)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymbolSearch(
  symbolSearchState: StockListState.SymbolSearchState,
  onQueryChanged: (String) -> Unit,
  onSuggestionClicked: (StockListState.SymbolSearchSuggestion) -> Unit
) {
  Column {
    TextField(
      modifier = Modifier.fillMaxWidth(),
      value = symbolSearchState.query,
      onValueChange = onQueryChanged,
      leadingIcon = {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = null
        )
      },
      placeholder = {
        Text(text = stringResource(id = R.string.search_for_a_symbol))
      }
    )
    DropdownMenu(
      modifier = Modifier.fillMaxWidth(),
      expanded = symbolSearchState.suggestions.isNotEmpty(),
      onDismissRequest = { },
      properties = PopupProperties(focusable = false)
    ) {
      symbolSearchState.suggestions.forEach {
        DropdownMenuItem(
          modifier = Modifier.fillMaxWidth(),
          text = { Text("${it.symbol} - ${it.description}") },
          onClick = { onSuggestionClicked(it) }
        )
      }
    }
  }
}