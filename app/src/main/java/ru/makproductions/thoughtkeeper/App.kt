package ru.makproductions.thoughtkeeper

import android.support.multidex.MultiDexApplication
import org.koin.android.ext.android.startKoin
import ru.makproductions.thoughtkeeper.di.modules.appModule
import ru.makproductions.thoughtkeeper.di.modules.mainModule
import ru.makproductions.thoughtkeeper.di.modules.noteModule
import ru.makproductions.thoughtkeeper.di.modules.splashModule
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
        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }

}