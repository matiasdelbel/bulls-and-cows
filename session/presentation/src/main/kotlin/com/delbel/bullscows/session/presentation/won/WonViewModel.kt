package com.delbel.bullscows.session.presentation.won

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.session.domain.CurrentSession
import com.delbel.bullscows.session.domain.repository.SessionRepository
import com.delbel.bullscows.session.presentation.di.AssistedViewModelFactory
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

internal class WonViewModel @AssistedInject constructor(
    @Assisted handle: SavedStateHandle,
    private val session: CurrentSession,
    private val sessionRepository: SessionRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedViewModelFactory<WonViewModel>

    private val gameId = GameId(id = handle.get<String>("game_id")!!.toLong())

    val game = liveData {
        val game = gameRepository.obtainGameBy(id = gameId)
        val sessionId = session.id()
        sessionRepository.addGameWon(sessionId, game)

        emit(game)
    }
}