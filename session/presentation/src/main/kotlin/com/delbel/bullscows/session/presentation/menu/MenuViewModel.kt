package com.delbel.bullscows.session.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.session.domain.repository.CurrentSessionRepository
import com.delbel.bullscows.session.domain.repository.SessionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class MenuViewModel @Inject constructor(
    private val currentSessionRepository: CurrentSessionRepository,
    private val sessionRepository: SessionRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    val sessionState = currentSessionRepository.obtainSessionId()
        .map { session -> if (session == null) NoSession else RunningSession }
        .asLiveData()

    fun create() = liveData {
        val gameId = gameRepository.create()
        val sessionId = sessionRepository.create()

        currentSessionRepository.register(sessionId, gameId)
        emit(value = NewSession(sessionId, gameId))
    }

    fun continueGame() = liveData {
        val gameId = currentSessionRepository.obtainGameId().first() ?: createGameAndSaveItOnSession()
        emit(value = gameId)
    }

    private suspend fun createGameAndSaveItOnSession(): GameId {
        val gameId = gameRepository.create()
        currentSessionRepository.updateGameId(gameId)

        return gameId
    }
}