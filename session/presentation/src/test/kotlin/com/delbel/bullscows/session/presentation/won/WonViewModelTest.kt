package com.delbel.bullscows.session.presentation.won

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.session.domain.Session
import com.delbel.bullscows.session.domain.SessionId
import com.delbel.bullscows.session.domain.repository.CurrentSessionRepository
import com.delbel.bullscows.session.domain.repository.SessionRepository
import com.delbel.bullscows.session.presentation.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WonViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun `game and session should obtain them and post them`() = coroutineRule.runBlockingTest {
        val savedState = mock<SavedStateHandle> { on { get<String>("game_id") } doReturn "123" }
        val session = mock<Session>()
        val sessionId = mock<SessionId>()
        val game = mock<Game>()
        val currentSessionRepository = mock<CurrentSessionRepository> {
            on { obtainSessionId() } doReturn flow { emit(value = sessionId) }
        }
        val sessionRepository = mock<SessionRepository> {
            on { obtainBy(sessionId) } doReturn flow { emit(value = session) }
        }
        val gameRepository = mock<GameRepository> {
            onBlocking { obtainGameBy(id = GameId(id = 123)) } doReturn game
        }
        val viewModel = WonViewModel(savedState, currentSessionRepository, sessionRepository, gameRepository)

        val sessionObserver = mock<Observer<Session>>()
        val gameObserver = mock<Observer<Game>>()
        viewModel.session.observeForever(sessionObserver)
        viewModel.game.observeForever(gameObserver)

        argumentCaptor<Session> {
            verify(sessionObserver).onChanged(capture())
            assertThat(firstValue).isEqualTo(session)
        }
        argumentCaptor<Game> {
            verify(gameObserver).onChanged(capture())
            assertThat(firstValue).isEqualTo(game)
        }
        verify(sessionRepository).addGameWon(id = sessionId, game = game)
    }

    @Test
    fun `createGame update game and notify id`() = coroutineRule.runBlockingTest {
        val savedState = mock<SavedStateHandle> { on { get<String>("game_id") } doReturn "123" }
        val currentSessionRepository = mock<CurrentSessionRepository> ()
        val gameId = mock<GameId>()
        val gameRepository = mock<GameRepository> {
            onBlocking { obtainGameBy(id = GameId(id = 123)) } doReturn mock()
            onBlocking { create() } doReturn gameId
        }
        val viewModel = WonViewModel(savedState, currentSessionRepository, mock(), gameRepository)
        val observer = mock<Observer<GameId>>()

        viewModel.createGame().observeForever(observer)

        argumentCaptor<GameId> {
            verify(observer).onChanged(capture())
            assertThat(firstValue).isEqualTo(gameId)
        }
        verify(currentSessionRepository).updateGameId(gameId)
    }
}