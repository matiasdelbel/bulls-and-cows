package com.delbel.bullscows.session.presentation.menu

import androidx.lifecycle.*
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.session.domain.repository.SessionIdRepository
import com.delbel.bullscows.session.domain.repository.SessionRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class MenuViewModel @Inject constructor(
    private val sessionIdRepository: SessionIdRepository,
    private val sessionRepository: SessionRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _sessionState = MutableLiveData<SessionState>()
    val sessionState: LiveData<SessionState> get() = _sessionState

    init {
        notifyCurrentSessionState()
    }

    private fun notifyCurrentSessionState() = viewModelScope.launch {
        runCatching { sessionIdRepository.obtainCurrentOrThrow(exception = RuntimeException()) }
            .onSuccess { _sessionState.value = RunningSession }
            .onFailure { _sessionState.value = NoSession }
    }

    fun create() = liveData {
        val gameId = gameRepository.create()
        val sessionId = sessionRepository.create()
        sessionIdRepository.registerCurrent(id = sessionId)

        emit(value = NewSession(sessionId, gameId))
    }
}