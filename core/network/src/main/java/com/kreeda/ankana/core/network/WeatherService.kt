package com.kreeda.ankana.core.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

// OpenWeatherMap API Models
data class WeatherResponse(
    val list: List<ForecastItem>
)

data class ForecastItem(
    val dt: Long,
    val main: MainWeather,
    val weather: List<WeatherCondition>
)

data class MainWeather(
    val temp: Double
)

data class WeatherCondition(
    val main: String,
    val description: String,
    val icon: String
)

interface OpenWeatherApi {
    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}

@Singleton
class WeatherService @Inject constructor() {
    private val apiKey = "7776da07921499de48583da7c2bacacf"
    private val api: OpenWeatherApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        api = retrofit.create(OpenWeatherApi::class.java)
    }

    suspend fun getWeatherForecast(lat: Double = 12.9716, lon: Double = 77.5946): Result<WeatherResponse> = runCatching {
        api.getForecast(lat = lat, lon = lon, appId = apiKey)
    }
}
