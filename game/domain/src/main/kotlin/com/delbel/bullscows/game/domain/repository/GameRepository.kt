package com.delbel.bullscows.game.domain.repository

import androidx.lifecycle.LiveData
import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.Shift

interface GameRepository {

    suspend fun game(id: GameId): Game

    fun shifts(id: GameId): LiveData<List<Shift>>

    suspend fun update(id: GameId, shift: Shift)
}