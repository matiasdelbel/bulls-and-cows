package com.delbel.bullscows.session.presentation.menu

import org.junit.Test

class MenuViewModelTest {

    @Test
    fun `create should create a new session and add it a new game`() {

    }
}

/*
internal class MenuViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    val session = sessionRepository.current().asLiveData()

    fun create() = liveData {
        val session = sessionRepository.create()
        startNewGameOnCurrent()

        emit(session)
    }

    fun startNewGameOnCurrent() = viewModelScope.launch {
        val game = gameRepository.create()
        sessionRepository.addGameToCurrent(game)
    }
}

 */