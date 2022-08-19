package dev.ahart.stockticker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.ahart.stockticker.list.StockListScreen
import dev.ahart.stockticker.list.StockListScreenViewModel
import dev.ahart.stockticker.ui.theme.StockTickerTheme

class MainActivity : ComponentActivity() {

  private val viewModel: StockListScreenViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      StockTickerTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          val stockListScreenState by viewModel.uiState.collectAsState()
          StockListScreen(stockListScreenState)
        }
      }
    }
  }
}
