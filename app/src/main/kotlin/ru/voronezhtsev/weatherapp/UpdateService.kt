package ru.voronezhtsev.weatherapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*
import ru.voronezhtsev.weatherapp.Application.Companion.weatherDatabase
import ru.voronezhtsev.weatherapp.Application.Companion.weatherService
import ru.voronezhtsev.weatherapp.Constants.UPDATE_ACTION
import ru.voronezhtsev.weatherapp.Constants.UPDATE_TIME_MS
import ru.voronezhtsev.weatherapp.db.Weather
import java.util.*

class UpdateService : Service() {

    private var job: Job = Job()
    /**
     * https://kenkyee.medium.com/android-kotlin-coroutine-best-practices-bc033fed62e7
     */
    private val serviceScope = CoroutineScope(Dispatchers.IO + job)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        job = serviceScope.launch {
            update()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private suspend fun update() {
        val intent = Intent(UPDATE_ACTION)
        while (job.isActive) {
            serviceScope.launch {
                try {
                    val response = weatherService.load()
                    weatherDatabase.weatherDao().insert(
                        Weather(
                            1,
                            response.name,
                            response.main.temp,
                            response.weather[0].icon,
                            response.weather[0].description,
                            Date().toString()
                        )
                    )
                    sendBroadcast(intent)
                    Log.i("UpdateService", "sendBroadcast")
                } catch (e: Exception) {
                    Log.e("UpdateService", "error loading weather: " + e.stackTraceToString())
                }
            }
            delay(UPDATE_TIME_MS)
            Log.i("UpdateService", "delay")
        }
        stopSelf()
    }

}