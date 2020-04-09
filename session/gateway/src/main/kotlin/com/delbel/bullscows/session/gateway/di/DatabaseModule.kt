package com.delbel.bullscows.session.gateway.di

import android.app.Application
import androidx.room.Room
import com.delbel.bullscows.session.gateway.database.SessionDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class DatabaseModule {

    companion object {
        private const val DATABASE_NAME = "session.db"
    }

    @Provides
    @Singleton
    fun provideDatabase(application: Application): SessionDatabase = Room
        .databaseBuilder(application, SessionDatabase::class.java, DATABASE_NAME)
        .build()

    @Provides
    fun provideSessionDao(database: SessionDatabase) = database.sessionDao()
}