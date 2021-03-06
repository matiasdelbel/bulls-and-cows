package com.delbel.bullscows.session.presentation.lost

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
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LostViewModelTest {

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
        val viewModel = LostViewModel(savedState, currentSessionRepository, sessionRepository, gameRepository)

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
        verify(currentSessionRepository).clear()
    }

    @Test
    fun `createSession should create it and notify current game`() = coroutineRule.runBlockingTest {
        val savedState = mock<SavedStateHandle> { on { get<String>("game_id") } doReturn "123" }
        val sessionId = mock<SessionId>()
        val currentSessionRepository = mock<CurrentSessionRepository>()
        val sessionRepository = mock<SessionRepository> { onBlocking { create() } doReturn sessionId }
        val gameId = mock<GameId>()
        val gameRepository = mock<GameRepository> {
            onBlocking { obtainGameBy(id = GameId(id = 123)) } doReturn mock()
            onBlocking { create() } doReturn gameId
        }
        val viewModel = LostViewModel(savedState, currentSessionRepository, sessionRepository, gameRepository)
        val observer = mock<Observer<GameId>>()

        viewModel.createSession().observeForever(observer)

        argumentCaptor<GameId> {
            verify(observer).onChanged(capture())
            assertThat(firstValue).isEqualTo(gameId)
        }
        verify(currentSessionRepository).register(sessionId, gameId)
    }
}