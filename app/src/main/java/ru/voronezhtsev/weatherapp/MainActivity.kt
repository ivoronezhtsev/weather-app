package ru.voronezhtsev.weatherapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val temp = findViewById<TextView>(R.id.temp)
        val weatherService = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)
        weatherService.load().enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.weather?.get(0)?.icon?.let {
                        findViewById<ImageView>(R.id.icon).setBackgroundResource(getIcon(it))
                    }
                    temp.text = response.body()?.main?.temp?.minus(273.15)?.roundToInt().toString()
                    findViewById<TextView>(R.id.city_name).text = response.body()?.name
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                temp.text = getString(R.string.error)
            }
        })
    }

    private fun getIcon(code: String): Int {
        when (code) {
            "01n" -> return R.drawable.ic_01n
            "02n" -> return R.drawable.ic_02n
            "03n" -> return R.drawable.ic_03n
            "04n" -> return R.drawable.ic_04n
            "09n" -> return R.drawable.ic_09n
            "10n" -> return R.drawable.ic_10n
            "11n" -> return R.drawable.ic_11n
            "13n" -> return R.drawable.ic_13n
            "50n" -> return R.drawable.ic_50n
            "01d" -> return R.drawable.ic_01d
            "02d" -> return R.drawable.ic_02d
            "03d" -> return R.drawable.ic_03d
            "04d" -> return R.drawable.ic_04d
            "09d" -> return R.drawable.ic_09d
            "10d" -> return R.drawable.ic_10d
            "11d" -> return R.drawable.ic_11d
            "13d" -> return R.drawable.ic_13d
            "50d" -> return R.drawable.ic_50d
            //todo Некоторые иконки дублируют друг друга
        }
        return R.drawable.ic_01d //todo Заглушка иконки
    }
}