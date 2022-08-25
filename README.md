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

## How would I use this as a template?

Honestly, this should be easy as removing *my* Kotlin code and replacing it with *yours*. You'll
also want to update the `applicationId` in your build script as appropriate, and double check the Manifest. 
You can then refer back to my original code to see things like how to set up your Application class so it
works with Hilt and WorkManager, etc. I've not actually tested this though, so YMMV.

## Why?

I wrote this mainly as a way to invest some time into learing compose and some other
libraries that I do not use in my day to day work. This app is built with the following:

- Compose
- DataStore (with Protobuf)
- WorkManager
- Room
- Hilt
- Retrofit
- Moshi

## What's Next

My intention from here is to add more polish. Mainly around things like Keyboard dismissal, etc.
More excuses to learn more about building out UI with compose. I'd also like to add a detail screen
for stocks as an excuse to do some navigation.

Further than that, I hope to add some unit and integration tests using JUnit and Espresso.

## I think I have a suggestion or improvement

I welcome all feedback, but don't plan to accept external contributions at this time. This is meant to
be my learning surface for these different libraries, and it's important to me that I'm the one writing
each line of code committed within it. However, please feel free to utilize Github's comment feature or
reach out to me on Twitter at `@exallium`
