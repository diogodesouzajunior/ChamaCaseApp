package com.diogo.chamacaseapp.injection.module

import com.diogo.chamacaseapp.data.DataManager
import com.diogo.chamacaseapp.data.remote.SimpleBusEvent
import com.diogo.chamacaseapp.remote.service.ChamaCaseService
import dagger.Module
import dagger.Provides

@Module
class DbModule {


    @Provides
    fun provideDataManager(
        service: ChamaCaseService,
        eventBus: SimpleBusEvent
    ) = DataManager(service, eventBus)
}
