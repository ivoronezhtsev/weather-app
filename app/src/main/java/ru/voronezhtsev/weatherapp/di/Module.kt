package ru.voronezhtsev.weatherapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.voronezhtsev.weatherapp.WeatherService
import ru.voronezhtsev.weatherapp.db.WeatherDatabase

@Module
class Module(private val context: Context) {

    @Provides
    fun provideWeatherDatabase(): WeatherDatabase {
        return Room
            .databaseBuilder(context, WeatherDatabase::class.java, "weather-db")
            .allowMainThreadQueries() //todo Убрать?
            .build()
    }

    @Provides
    fun provideWeatherService(): WeatherService {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)
    }
}
