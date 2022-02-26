package ru.voronezhtsev.weatherapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.voronezhtsev.weatherapp.Constants.NULL_WEATHER
import ru.voronezhtsev.weatherapp.Constants.PENDING_INTENT_NAME
import ru.voronezhtsev.weatherapp.Constants.UPDATE_WEATHER_EVENT
import ru.voronezhtsev.weatherapp.db.Weather
import ru.voronezhtsev.weatherapp.db.WeatherDatabase
import javax.inject.Inject
import kotlin.math.roundToInt

object Constants {
    const val PENDING_INTENT_NAME = "pendingIntent"
    const val UPDATE_WEATHER_EVENT = 1
    const val UPDATE_TIME_MS = 10000L
    val NULL_WEATHER = Weather(1, "", 0.0, "", "", "")
}

class MainActivity : AppCompatActivity() {
    private lateinit var startServiceIntent: Intent

    @Inject
    lateinit var weatherDatabase: WeatherDatabase
    private val updateWeatherRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as Application).component.inject(this)

        val pendingIntent = createPendingResult(updateWeatherRequestCode, Intent(), 0)
        startServiceIntent = Intent(this, UpdateService::class.java)
        startServiceIntent.putExtra(PENDING_INTENT_NAME, pendingIntent)
        startService(startServiceIntent)
    }

    //todo Устарел, гуглить новый способ оповещения активити при изменении данных сервисом
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == updateWeatherRequestCode && resultCode == UPDATE_WEATHER_EVENT) {
            weatherDatabase.weatherDao().find()?.let { setWeather(it) }
        }
    }

    private fun setWeather(weather: Weather) {
        val tempTv = findViewById<TextView>(R.id.temp)
        if (weather != NULL_WEATHER) {
            findViewById<ImageView>(R.id.icon).setBackgroundResource(getIcon(weather.icon))
            findViewById<TextView>(R.id.description).text = weather.description
            tempTv.text =
                weather.temp.minus(273.15).roundToInt().toString()
            findViewById<TextView>(R.id.city_name).text = weather.name
            findViewById<TextView>(R.id.weather_date).text = weather.dateTime
        } else {
            tempTv.text = getString(R.string.error)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(startServiceIntent)
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