package com.diogo.chamacaseapp.injection.component

import android.app.Application
import android.content.Context
import com.diogo.chamacaseapp.data.DataManager
import com.diogo.chamacaseapp.injection.ApplicationContext
import com.diogo.chamacaseapp.injection.module.ApiModule
import com.diogo.chamacaseapp.injection.module.ApplicationModule
import com.diogo.chamacaseapp.injection.module.DataModule
import com.diogo.chamacaseapp.injection.module.PresenterModule
import com.diogo.chamacaseapp.remote.service.ChamaCaseService
import com.diogo.chamacaseapp.service.GPSMonitorService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, DataModule::class, ApiModule::class])
interface ApplicationComponent {

    fun inject(service: GPSMonitorService)

    @ApplicationContext
    fun context(): Context
    fun application(): Application
    fun chamaCaseService(): ChamaCaseService
    fun dataManager(): DataManager

    operator fun plus(presenterModule: PresenterModule): ConfigPersistentComponent
}
