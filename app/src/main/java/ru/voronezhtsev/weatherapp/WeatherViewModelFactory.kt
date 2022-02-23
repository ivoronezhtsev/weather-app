package ru.voronezhtsev.weatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.voronezhtsev.weatherapp.db.WeatherDatabase

class WeatherViewModelFactory(
    private val weatherService: WeatherService,
    private val weatherDatabase: WeatherDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(WeatherService::class.java, WeatherDatabase::class.java)
            .newInstance(
                weatherService,
                weatherDatabase
            )
    }
}