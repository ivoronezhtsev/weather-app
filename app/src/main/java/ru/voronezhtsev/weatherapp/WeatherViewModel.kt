package ru.voronezhtsev.weatherapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.voronezhtsev.weatherapp.db.WeatherDatabase

class WeatherViewModel(private val weatherService: WeatherService) : ViewModel() {
    val weatherLiveData: MutableLiveData<WeatherResponse?> by lazy {
        MutableLiveData<WeatherResponse?>().also {
            loadWeather()
        }
    }

    private fun loadWeather() {
        weatherService.load().enqueue(object : Callback<WeatherResponse?> {
            override fun onResponse(
                call: Call<WeatherResponse?>,
                response: Response<WeatherResponse?>
            ) {
                if (response.isSuccessful) {
                    weatherLiveData.value = response.body()
                    /*response.body()?.let {
                        weatherDatabase.weatherDao().insertAll(
                            Weather(1, it.main.temp,
                                it.weather[0].icon, it.weather[0].description, Date().toString())
                        )
                    }*/
                }
            }

            override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
                weatherLiveData.value = null
            }
        })
    }
}