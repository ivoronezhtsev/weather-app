package ru.voronezhtsev.weatherapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather")
    fun find(): Weather?

    @Insert(onConflict = REPLACE)
    fun insertAll(vararg weather: Weather)

    @Delete
    fun delete(weather: Weather)

}