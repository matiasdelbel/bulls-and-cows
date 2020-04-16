package com.delbel.bullscows.session.presentation.menu

import androidx.lifecycle.*
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.session.domain.repository.CurrentSessionRepository
import com.delbel.bullscows.session.domain.repository.SessionRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class MenuViewModel @Inject constructor(
    private val currentSessionRepository: CurrentSessionRepository,
    private val sessionRepository: SessionRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _sessionState = MutableLiveData<SessionState>()
    val sessionState: LiveData<SessionState> get() = _sessionState

    init {
        notifyCurrentSessionState()
    }

    private fun notifyCurrentSessionState() = viewModelScope.launch {
        runCatching { currentSessionRepository.obtainSessionIdOrThrow(exception = RuntimeException()) }
            .onSuccess { _sessionState.value = RunningSession }
            .onFailure { _sessionState.value = NoSession }
    }

    fun create() = liveData {
        val gameId = gameRepository.create()
        val sessionId = sessionRepository.create()
        currentSessionRepository.register(sessionId, gameId)

        emit(value = NewSession(sessionId, gameId))
    }

    fun continueGame() = liveData {
        runCatching { currentGameId() }
            .onSuccess { emit(value = it) }
            .onFailure { emit(value = createGameAndSaveItOnSession()) }
    }

    private suspend fun currentGameId(): GameId {
        val gameId = currentSessionRepository.obtainGameIdOrThrow(exception = RuntimeException())
        gameRepository.obtainGameBy(gameId).throwIfIsOver(exception = RuntimeException())

        return gameId
    }

    private suspend fun createGameAndSaveItOnSession(): GameId {
        val gameId = gameRepository.create()
        currentSessionRepository.updateGameId(gameId)

        return gameId
    }
}