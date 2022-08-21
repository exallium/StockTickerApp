package dev.ahart.stockticker.data.api

import dev.ahart.stockticker.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit wrapper for the required API from Finnhub
 */
interface FinnhubService {

  @GET("quote")
  suspend fun getQuote(@Query("symbol") symbol: String): FinnhubQuoteJson

  @GET("stock/symbol")
  suspend fun getSymbols(@Query("exchange") exchange: String): List<FinnhubSymbolJson>

  companion object {
    /**
     * Creates a new instance of FinnhubService which can be used to query the
     * polygon apis. Note that this service has a limit of 60req/min on the free
     * tier plan.
     */
    fun create(): FinnhubService {
      val client = OkHttpClient.Builder().addInterceptor(ApiKeyInterceptor()).build()
      val retrofit = Retrofit.Builder()
        .baseUrl("https://finnhub.io/api/v1/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

      return retrofit.create(FinnhubService::class.java)
    }
  }

  private class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
      val requestBuilder = chain.request().newBuilder()
      requestBuilder.addHeader("X-Finnhub-Token", BuildConfig.FINNHUB_API_KEY)
      return chain.proceed(requestBuilder.build())
    }
  }
}