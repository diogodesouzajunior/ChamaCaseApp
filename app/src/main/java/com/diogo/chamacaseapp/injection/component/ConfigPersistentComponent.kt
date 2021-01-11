package com.diogo.chamacaseapp.injection.component

import com.diogo.chamacaseapp.injection.ConfigPersistent
import com.diogo.chamacaseapp.injection.module.ActivityModule
import com.diogo.chamacaseapp.injection.module.PresenterModule
import com.diogo.chamacaseapp.ui.estabilishment.EstabilishmentFragment
import dagger.Subcomponent

/**
 * A dagger component that will live during the lifecycle of an Activity but it won't
 * be destroy during configuration changes. Check [BaseActivity] to see how this components
 * survives configuration changes.
 * Use the [ConfigPersistent] scope to annotate dependencies that need to survive
 * configuration changes (for example Presenters).
 */
@ConfigPersistent
@Subcomponent(modules = [PresenterModule::class])
interface ConfigPersistentComponent {
    operator fun plus(activityModule: ActivityModule): ActivityComponent


    fun inject(fragment: EstabilishmentFragment)
}
