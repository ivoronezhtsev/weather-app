package ru.voronezhtsev.weatherapp

import retrofit2.http.GET
import retrofit2.http.Query

data class WeatherResponse(val main: Main, val name: String, val weather: List<Weather>)
data class Main(val temp: Double)
data class Weather(val icon: String, val description: String)

interface WeatherService {

    @GET("data/2.5/weather")
    suspend fun load(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = "458a017c6453d7ee6e2cfa3a5ddec547",
    ): WeatherResponse

}