# StockTicker App

Simple app that displays a few FAANG stock tickers and allows the user to search for and
add different tickers to a watchlist.

## API Keys

This project utilizes [finnhub.io](https://finnhub.io) to access stock data. An API
key is required. However, this key *is* free and can be gotten by only signing up on
their website. Once you have the key, you can put it into your `local.properties` file:

```
FINNHUB_API_KEY=myFinnhubApiKey
```

This will automatically be added to your BuildConfig object after the next successful
build. (In my experience, just doing a gradle sync doesn't seem to generate it.)
