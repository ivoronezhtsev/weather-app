package ru.voronezhtsev.weatherapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weather(
    @PrimaryKey
    val id: Int,
    val name: String,
    val temp: Double,
    val icon: String,
    val description:String,
    val dateTime: String
)