package com.delbel.bullscows.game.gateway.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.delbel.bullscows.game.gateway.model.GameDo
import com.delbel.bullscows.game.gateway.model.ShiftDo

@Database(entities = [GameDo::class, ShiftDo::class], version = 1, exportSchema = false)
internal abstract class GameDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    abstract fun shiftDao(): ShiftDao
}