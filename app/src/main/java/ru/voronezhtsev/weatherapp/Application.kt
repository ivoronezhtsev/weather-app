package ru.voronezhtsev.weatherapp

import android.app.Application
import ru.voronezhtsev.weatherapp.di.Component
import ru.voronezhtsev.weatherapp.di.DaggerComponent
import ru.voronezhtsev.weatherapp.di.Module

class Application: Application() {
    lateinit var component: Component
    override fun onCreate() {
        super.onCreate()
        component = DaggerComponent.builder().module(Module(applicationContext)).build()
    }
}