package ru.voronezhtsev.weatherapp

import android.os.Bundle
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
        weatherService.load().enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                if (response.isSuccessful) {
                    //todo ?
                    temp.text = response.body()?.main?.temp?.minus(273.15)?.roundToInt().toString()
                    findViewById<TextView>(R.id.city_name).text = response.body()?.name
                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                temp.text = "Error"
            }
        })
    }
    
}