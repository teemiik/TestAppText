package com.example.testapp

import android.app.Application
import com.example.testapp.di.AppComponent

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appComponent = AppComponent()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}