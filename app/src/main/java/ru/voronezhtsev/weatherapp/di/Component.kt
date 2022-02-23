package ru.voronezhtsev.weatherapp.di

import dagger.Component
import ru.voronezhtsev.weatherapp.MainActivity
import ru.voronezhtsev.weatherapp.UpdateService

@Component(modules = [Module::class])
interface Component {
    fun inject(activity: MainActivity)
    fun inject(service: UpdateService)
}