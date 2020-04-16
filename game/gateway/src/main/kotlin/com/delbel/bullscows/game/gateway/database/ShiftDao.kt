package com.delbel.bullscows.game.gateway.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.delbel.bullscows.game.gateway.model.ShiftDo
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ShiftDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(shift: ShiftDo)

    @Query("SELECT * FROM shifts WHERE gameId = :gameId ORDER BY attempt DESC")
    fun obtainFor(gameId: Long): Flow<List<ShiftDo>>

    @Query("SELECT * FROM shifts WHERE gameId = :gameId ORDER BY attempt DESC LIMIT 1")
    suspend fun obtainLastFor(gameId: Long): ShiftDo?
}