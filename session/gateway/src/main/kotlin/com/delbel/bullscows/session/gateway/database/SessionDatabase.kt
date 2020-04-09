package com.delbel.bullscows.session.gateway.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.delbel.bullscows.session.gateway.model.SessionDo

@Database(entities = [SessionDo::class], version = 1, exportSchema = false)
internal abstract class SessionDatabase : RoomDatabase() {

    abstract fun sessionDao(): SessionDao
}