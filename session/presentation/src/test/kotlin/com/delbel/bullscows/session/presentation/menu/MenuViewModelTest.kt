package com.delbel.bullscows.session.presentation.menu

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.session.domain.SessionId
import com.delbel.bullscows.session.domain.repository.CurrentSessionRepository
import com.delbel.bullscows.session.domain.repository.SessionRepository
import com.delbel.bullscows.session.presentation.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class MenuViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun `init with running session should notify it`() = coroutineRule.runBlockingTest {
        val currentSessionRepository = mock<CurrentSessionRepository> {
            on { obtainSessionId() } doReturn flow { emit(value = mock<SessionId>()) }
        }
        val viewModel = MenuViewModel(currentSessionRepository, mock(), mock())
        val observer = mock<Observer<SessionState>>()

        viewModel.sessionState.observeForever(observer)

        argumentCaptor<SessionState> {
            verify(observer).onChanged(capture())
            assertThat(firstValue).isEqualTo(RunningSession)
        }
    }

    @Test
    fun `init without session should notify no session`() = coroutineRule.runBlockingTest {
        val currentSessionRepository = mock<CurrentSessionRepository> {
            on { obtainSessionId() } doReturn flow { emit(value = null) }
        }
        val viewModel = MenuViewModel(currentSessionRepository, mock(), mock())
        val observer = mock<Observer<SessionState>>()

        viewModel.sessionState.observeForever(observer)

        argumentCaptor<SessionState> {
            verify(observer).onChanged(capture())
            assertThat(firstValue).isEqualTo(NoSession)
        }
    }

    @Test
    fun `create should create a session and notify`() = coroutineRule.runBlockingTest {
        val currentSessionRepository = mock<CurrentSessionRepository> {
            on { obtainGameId() } doReturn mock()
        }
        val sessionId = mock<SessionId>()
        val sessionRepository = mock<SessionRepository> {
            onBlocking { create() } doReturn sessionId
        }
        val gameId = mock<GameId>()
        val gameRepository = mock<GameRepository> { onBlocking { create() } doReturn gameId }
        val viewModel = MenuViewModel(currentSessionRepository, sessionRepository, gameRepository)
        val observer = mock<Observer<SessionState>>()

        viewModel.create().observeForever(observer)

        argumentCaptor<SessionState> {
            verify(observer).onChanged(capture())
            assertThat(firstValue).isEqualTo(NewSession(sessionId, gameId))
        }
        verify(currentSessionRepository).register(sessionId, gameId)
    }

    @Test
    fun `continueGame should return current game id`() = coroutineRule.runBlockingTest {
        val gameId = mock<GameId>()
        val currentSessionRepository = mock<CurrentSessionRepository> {
            on { obtainGameId() } doReturn flow { emit(value = gameId) }
        }
        val gameRepository = mock<GameRepository> {
            onBlocking { obtainGameBy(gameId) } doReturn mock()
        }
        val viewModel = MenuViewModel(currentSessionRepository, mock(), gameRepository)
        val observer = mock<Observer<GameId>>()

        viewModel.continueGame().observeForever(observer)

        argumentCaptor<GameId> {
            verify(observer).onChanged(capture())
            assertThat(firstValue).isEqualTo(gameId)
        }
    }

    @Test
    fun `continueGame with game should return it`() = coroutineRule.runBlockingTest {
        val gameId = mock<GameId>()
        val currentSessionRepository = mock<CurrentSessionRepository> {
            on { obtainGameId() } doReturn flow { emit(value = gameId) }
        }
        val viewModel = MenuViewModel(currentSessionRepository, mock(), mock())
        val observer = mock<Observer<GameId>>()

        viewModel.continueGame().observeForever(observer)

        argumentCaptor<GameId> {
            verify(observer).onChanged(capture())
            assertThat(firstValue).isEqualTo(gameId)
        }
    }

    @Test
    fun `continueGame with game over should return new game id`() = coroutineRule.runBlockingTest {
        val currentSessionRepository = mock<CurrentSessionRepository> {
            on { obtainGameId() } doReturn flow { emit(value = null) }
        }
        val newGameId = mock<GameId>()
        val gameRepository = mock<GameRepository> { onBlocking { create() } doReturn newGameId }
        val viewModel = MenuViewModel(currentSessionRepository, mock(), gameRepository)
        val observer = mock<Observer<GameId>>()

        viewModel.continueGame().observeForever(observer)

        argumentCaptor<GameId> {
            verify(observer).onChanged(capture())
            assertThat(firstValue).isEqualTo(newGameId)
        }
        verify(currentSessionRepository).updateGameId(newGameId)
    }
}