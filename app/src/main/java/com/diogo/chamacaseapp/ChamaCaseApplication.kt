package com.diogo.chamacaseapp

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.diogo.chamacaseapp.injection.component.ApplicationComponent
import com.diogo.chamacaseapp.injection.component.DaggerApplicationComponent
import com.diogo.chamacaseapp.injection.module.ApplicationModule
import timber.log.BuildConfig
import timber.log.Timber

open class ChamaCaseApplication : Application() {

    companion object {
        var username: String? = null
    }

    lateinit var applicationComponent: ApplicationComponent
        private set


    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        initDaggerComponent()
    }

    @VisibleForTesting
    fun initDaggerComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }
}
