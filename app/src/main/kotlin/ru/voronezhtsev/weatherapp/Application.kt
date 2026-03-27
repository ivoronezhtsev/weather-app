package ru.voronezhtsev.weatherapp

import android.app.Application
import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging) // Добавляем логгер в клиент
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // Важно: передаем наш клиент с логгером
            .build()
        weatherService = retrofit.create(WeatherService::class.java)
    }

}