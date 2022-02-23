package ru.voronezhtsev.weatherapp

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.voronezhtsev.weatherapp.Constants.PENDING_INTENT_NAME
import ru.voronezhtsev.weatherapp.Constants.UPDATE_WEATHER_EVENT
import ru.voronezhtsev.weatherapp.db.Weather
import ru.voronezhtsev.weatherapp.db.WeatherDatabase
import java.util.*
import javax.inject.Inject

class UpdateService : Service() {
    private lateinit var thread: Thread
    private var pendingIntent: PendingIntent? = null
    @Inject
    lateinit var weatherService: WeatherService
    @Inject
    lateinit var database: WeatherDatabase
    override fun onCreate() {
        super.onCreate()
        (application as Application).component.inject(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null;
    }

    private val tag = "UpdateService"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(tag, "onStartCommand")
        thread = Thread { update() }
        thread.start()
        intent?.let {
            pendingIntent = it.getParcelableExtra(PENDING_INTENT_NAME)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "onDestroy")
        thread.interrupt()
    }

    private fun update() {
        while (!Thread.currentThread().isInterrupted) {
            Log.d(tag, "update")
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
                            pendingIntent?.send(UPDATE_WEATHER_EVENT)
                            database.weatherDao().insertAll(weather)
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
                    // В случае ошибки в БД ничего не пишем
                    //todo Мб оповещать тоже не стоит?
                    pendingIntent?.send(UPDATE_WEATHER_EVENT)
                }
            })
            try {
                Thread.sleep(Constants.UPDATE_TIME_MS)
            } catch (e: InterruptedException) {
                Log.d(tag, "interrupt")
                Thread.currentThread().interrupt()
            }
        }
        stopSelf() //todo Не знаю надо ли
    }
}