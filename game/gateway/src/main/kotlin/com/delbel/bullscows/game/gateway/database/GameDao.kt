package com.delbel.bullscows.game.gateway.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.delbel.bullscows.game.gateway.model.GameDo

@Dao
internal interface GameDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(game: GameDo): Long

    @Query("SELECT * from games WHERE id = :gameId")
    suspend fun obtainBy(gameId: Long): GameDo
}