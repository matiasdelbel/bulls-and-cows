package com.delbel.bullscows.session.gateway.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.delbel.bullscows.session.gateway.model.SessionDo
import kotlinx.coroutines.flow.Flow

@Dao
internal interface SessionDao {

    @Query("SELECT * from session WHERE id = :sessionId")
    fun obtainBy(sessionId: Long): Flow<SessionDo>

    @Query("SELECT * from session ORDER BY points DESC LIMIT 10")
    fun obtainAll(): Flow<List<SessionDo>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(session: SessionDo): Long

    @Update
    suspend fun update(session: SessionDo)
}