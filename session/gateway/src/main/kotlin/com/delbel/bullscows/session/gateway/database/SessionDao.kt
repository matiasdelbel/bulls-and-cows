package com.delbel.bullscows.session.gateway.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.delbel.bullscows.session.gateway.model.SessionDo

@Dao
internal interface SessionDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(session: SessionDo): Long

    @Query("SELECT * from session WHERE id = :sessionId")
    suspend fun obtainBy(sessionId: Long): SessionDo

    @Query("UPDATE session SET guessed = :guessed AND points = :points WHERE id = :sessionId")
    suspend fun update(sessionId: Long, guessed: Int, points: Int)
}