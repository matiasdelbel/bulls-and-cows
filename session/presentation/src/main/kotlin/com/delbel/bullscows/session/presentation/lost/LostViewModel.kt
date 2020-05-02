package com.delbel.bullscows.session.presentation.lost

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.session.domain.repository.CurrentSessionRepository
import com.delbel.bullscows.session.domain.repository.SessionRepository
import com.delbel.bullscows.session.presentation.di.AssistedViewModelFactory
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.flow.flatMapLatest

internal class LostViewModel @AssistedInject constructor(
    @Assisted handle: SavedStateHandle,
    private val currentSessionRepository: CurrentSessionRepository,
    private val sessionRepository: SessionRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedViewModelFactory<LostViewModel>

    val session = currentSessionRepository.obtainSessionId()
        .flatMapLatest { sessionId -> sessionRepository.obtainBy(sessionId!!) }
        .asLiveData()

    val game = liveData {
        val gameId = GameId(id = handle.get<String>("game_id")!!.toLong())
        val game = gameRepository.obtainGameBy(id = gameId)
        currentSessionRepository.clear()

        emit(game)
    }

    fun createSession() = liveData {
        val sessionId = sessionRepository.create()
        val gameId = gameRepository.create()

        currentSessionRepository.register(sessionId, gameId)

        emit(gameId)
    }
}