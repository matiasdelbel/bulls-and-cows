package com.delbel.bullscows.game.domain.repository

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.Shift
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    suspend fun create(): Game

    suspend fun obtainGameBy(id: GameId): Game

    suspend fun addShift(gameId: GameId, shift: Shift)

    fun shiftsFor(id: GameId): Flow<List<Shift>>
}