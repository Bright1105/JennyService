package com.example.jennyserviceapp

import android.app.Application
import com.example.jennyserviceapp.data.AppContainer
import com.example.jennyserviceapp.data.DefaultAppContainer

class ServiceApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        container = DefaultAppContainer(this)
    }
}