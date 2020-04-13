package com.delbel.bullscows.session.gateway.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
internal class PreferencesModule {

    companion object {
        private const val PREFERENCES_NAME = "session"
    }

    @Provides
    fun providePreferences(application: Application) =
        application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
}