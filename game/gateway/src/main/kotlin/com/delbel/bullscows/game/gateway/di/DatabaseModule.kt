package com.delbel.bullscows.game.gateway.di

import android.app.Application
import androidx.room.Room
import com.delbel.bullscows.game.gateway.database.GameDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class DatabaseModule {

    companion object {
        private const val DATABASE_NAME = "game.db"
    }

    @Provides
    @Singleton
    fun provideDatabase(application: Application): GameDatabase =
        Room.databaseBuilder(application, GameDatabase::class.java, DATABASE_NAME)
            .build()

    @Provides
    fun provideGameDao(database: GameDatabase) = database.gameDao()

    @Provides
    fun provideShiftDao(database: GameDatabase) = database.shiftDao()
}