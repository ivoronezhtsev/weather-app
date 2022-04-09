package ru.voronezhtsev.weatherapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.voronezhtsev.weatherapp.Application.Companion.weatherDatabase
import ru.voronezhtsev.weatherapp.Constants.UPDATE_ACTION
import ru.voronezhtsev.weatherapp.db.Weather
import kotlin.math.roundToInt

object Constants {
    const val UPDATE_TIME_MS = 20000L
    const val UPDATE_ACTION = "ru.voronezhtsev.weatherapp.action.update"
}

class MainActivity : AppCompatActivity() {
    private lateinit var startServiceIntent: Intent
    private lateinit var broadcastReceiver: BroadcastReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startServiceIntent = Intent(this, UpdateService::class.java)
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                lifecycleScope.launch {
                    weatherDatabase.weatherDao().find()?.let {
                        setWeather(it)
                    }
                }
            }
        }
        lifecycleScope.launch {
            val weather = weatherDatabase.weatherDao().find()
            weather?.let {
                setWeather(it)
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter(UPDATE_ACTION))
        startService(startServiceIntent)
    }

    private fun setWeather(weather: Weather) {
        val tempTv = findViewById<TextView>(R.id.temp)
        findViewById<ImageView>(R.id.icon).setBackgroundResource(getIcon(weather.icon))
        findViewById<TextView>(R.id.description).text = weather.description
        tempTv.text =
            weather.temp.minus(273.15).roundToInt().toString()
        findViewById<TextView>(R.id.city_name).text = weather.name
        findViewById<TextView>(R.id.weather_date).text = weather.dateTime
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
        stopService(startServiceIntent)
    }

    private fun getIcon(code: String): Int {
        when (code) {
            "01n" -> return R.drawable.ic_01n
            "02n" -> return R.drawable.ic_02n
            "03n" -> return R.drawable.ic_03d
            "04n" -> return R.drawable.ic_04d
            "09n" -> return R.drawable.ic_09d
            "10n" -> return R.drawable.ic_10n
            "11n" -> return R.drawable.ic_11n
            "13n" -> return R.drawable.ic_13d
            "50n" -> return R.drawable.ic_50d
            "01d" -> return R.drawable.ic_01d
            "02d" -> return R.drawable.ic_02d
            "03d" -> return R.drawable.ic_03d
            "04d" -> return R.drawable.ic_04d
            "09d" -> return R.drawable.ic_09d
            "10d" -> return R.drawable.ic_10d
            "11d" -> return R.drawable.ic_11d
            "13d" -> return R.drawable.ic_13d
            "50d" -> return R.drawable.ic_50d
        }
        return R.drawable.ic_01d //todo Заглушка иконки на случай ошибки
    }
}