package ru.voronezhtsev.weatherapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.voronezhtsev.weatherapp.Constants.NULL_WEATHER
import ru.voronezhtsev.weatherapp.Constants.UPDATE_ACTION
import ru.voronezhtsev.weatherapp.db.Weather
import ru.voronezhtsev.weatherapp.db.WeatherDatabase
import java.util.*
import javax.inject.Inject

class UpdateService : Service() {
    private lateinit var thread: Thread

    @Inject
    lateinit var weatherService: WeatherService

    @Inject
    lateinit var weatherDatabase: WeatherDatabase

    override fun onCreate() {
        super.onCreate()
        (application as Application).component.inject(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        thread = Thread {
            update()
        }
        thread.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        thread.interrupt()
    }

    private fun update() {
        val intent = Intent(UPDATE_ACTION)
        while (!Thread.currentThread().isInterrupted) {
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
                            runBlocking {
                                weatherDatabase.weatherDao().insert(weather)
                            }
                        }
                    } else {
                        runBlocking {
                            weatherDatabase.weatherDao().insert(NULL_WEATHER)
                        }
                    }
                    sendBroadcast(intent)
                }

                override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
                    runBlocking {
                        weatherDatabase.weatherDao().insert(NULL_WEATHER)
                    }
                    sendBroadcast(intent)
                }
            })
            try {
                Thread.sleep(Constants.UPDATE_TIME_MS)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }
}