package com.diogo.chamacaseapp.injection.module

import dagger.Module

@Module(includes = [ApiModule::class, DbModule::class])
class DataModule {

    /**
     * Prove o sharedPreferences
     */
}
