package ru.voronezhtsev.weatherapp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [Weather::class])
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao() : WeatherDao
}