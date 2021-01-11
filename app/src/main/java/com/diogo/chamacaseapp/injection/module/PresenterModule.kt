package com.diogo.chamacaseapp.injection.module

import com.diogo.chamacaseapp.data.DataManager
import com.diogo.chamacaseapp.injection.ConfigPersistent
import com.diogo.chamacaseapp.ui.estabilishment.EstabilishmentContract
import com.diogo.chamacaseapp.ui.estabilishment.EstabilishmentPresenter
import dagger.Module
import dagger.Provides


@Module
class PresenterModule {

    @Provides
    @ConfigPersistent
    fun providesRestaurantPresenter(dataManager: DataManager): EstabilishmentContract.Presenter {
        return EstabilishmentPresenter(dataManager)
    }

}