package com.diogo.chamacaseapp.injection.module

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.diogo.chamacaseapp.injection.ActivityContext
import com.diogo.chamacaseapp.injection.PerActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    @PerActivity
    internal fun provideActivity(): Activity {
        return activity
    }

    @Provides
    @PerActivity
    @ActivityContext
    internal fun providesContext(): Context {
        return activity
    }

    @Provides
    internal fun provideFragmentManager(): FragmentManager {
        return activity.supportFragmentManager
    }

}
