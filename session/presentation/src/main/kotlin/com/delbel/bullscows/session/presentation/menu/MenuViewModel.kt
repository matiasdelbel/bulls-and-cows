package com.delbel.bullscows.session.presentation.menu

import androidx.lifecycle.*
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.session.domain.repository.SessionRepository
import com.delbel.bullscows.session.presentation.menu.SessionState.*
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class MenuViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _state = MutableLiveData<SessionState>()
    val state: LiveData<SessionState> get() = _state

    val session = liveData {
        val session = obtainCurrentSession()

        session.current.executeIf(
            isAboutToStart = { emit(SessionWithGameInProgress(session.current.id)) },
            isInProgress = { emit(SessionWithGameInProgress(session.current.id)) },
            hasWon = {
                val game = gameRepository.create()
                sessionRepository.addGame(session.id, game)

                emit(SessionWithGameInProgress(session.current.id))
            },
            hasLost = { emit(SessionOver) })
    }

    fun createSession() = viewModelScope.launch {
        val game = gameRepository.create()
        sessionRepository.create(initialGame = game)

        _state.value = ResumingGame(gameId = game.id)
    }

    private suspend fun obtainCurrentSession() = sessionRepository.obtainLastSessionOrDefault {
        val game = gameRepository.create()
        val session = sessionRepository.create(initialGame = game)

        session
    }
}