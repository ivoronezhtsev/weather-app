package ru.voronezhtsev.weatherapp

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.voronezhtsev.weatherapp.db.Weather
import ru.voronezhtsev.weatherapp.db.WeatherDatabase
import java.util.*

class WeatherRepository(
    private val weatherService: WeatherService,
    private val weatherDatabase: WeatherDatabase
) {
    fun updateAndGet(): Weather? {
        val weatherFromDb = weatherDatabase.weatherDao().find()
        var result = weatherFromDb
        if (weatherFromDb == null) {
            weatherService.load().enqueue(object : Callback<WeatherResponse?> {
                override fun onResponse(
                    call: Call<WeatherResponse?>,
                    response: Response<WeatherResponse?>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val weather = Weather(
                                1, it.name, it.main.temp,
                                it.weather[0].icon, it.weather[0].description, Date().toString()
                            )
                            result = weather
                            weatherDatabase.weatherDao().insertAll(weather)
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
                    result = null
                }
            })
            return result
        } else {
            return weatherFromDb
        }
    }

    fun getFromDb(): Weather? {
        return weatherDatabase.weatherDao().find()
    }

    fun save(weather: Weather) {
        weatherDatabase.weatherDao().insertAll(weather)
    }
}