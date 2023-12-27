package ru.voronezhtsev.weatherapp

import retrofit2.http.GET

data class WeatherResponse(val main: Main, val name: String, val weather: List<Weather>)
data class Main(val temp: Double)
data class Weather(val icon: String, val description: String)

interface WeatherService {

    @GET("data/2.5/weather?id=473778&appid=458a017c6453d7ee6e2cfa3a5ddec547")
    suspend fun load(): WeatherResponse

}