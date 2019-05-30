package ru.makproductions.thoughtkeeper

import android.support.multidex.MultiDexApplication
import timber.log.Timber

class App : MultiDexApplication() {

    companion object {

        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Timber.plant(Timber.DebugTree())
    }

}