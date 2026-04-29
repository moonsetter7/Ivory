package com.example.ivorypiano

import android.app.Application
import com.example.ivorypiano.data.AppContainer
import com.example.ivorypiano.data.AppDataContainer

class IvoryPianoApplication : Application() {
    /**
     * AppContainer instance used by the rest of the classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
