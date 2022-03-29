package ru.voronezhtsev.weatherapp

import android.app.Application
import androidx.room.Room
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.voronezhtsev.weatherapp.db.WeatherDatabase

class Application : Application() {

    companion object {
        lateinit var weatherDatabase: WeatherDatabase
        lateinit var weatherService: WeatherService
    }

    override fun onCreate() {
        super.onCreate()
        weatherDatabase = Room
            .databaseBuilder(applicationContext, WeatherDatabase::class.java, "weather-db")
            .build()
        weatherService = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)
    }

}