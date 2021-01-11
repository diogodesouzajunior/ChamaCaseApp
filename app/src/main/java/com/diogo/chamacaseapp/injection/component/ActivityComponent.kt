package com.diogo.chamacaseapp.injection.component

import com.diogo.chamacaseapp.injection.PerActivity
import com.diogo.chamacaseapp.injection.module.ActivityModule
import com.diogo.chamacaseapp.ui.activity.MainActivity

import dagger.Subcomponent

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity: MainActivity)
}