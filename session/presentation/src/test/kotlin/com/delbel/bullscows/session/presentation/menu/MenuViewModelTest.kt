package com.delbel.bullscows.session.presentation.menu

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.session.domain.SessionId
import com.delbel.bullscows.session.domain.repository.SessionIdRepository
import com.delbel.bullscows.session.domain.repository.SessionRepository
import com.delbel.bullscows.session.presentation.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
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
        val sessionIdRepository = mock<SessionIdRepository> {
            onBlocking { obtainCurrentOrThrow(any()) } doReturn mock()
        }
        val viewModel = MenuViewModel(sessionIdRepository, mock(), mock())
        val observer = mock<Observer<SessionState>>()

        viewModel.sessionState.observeForever(observer)

        argumentCaptor<SessionState> {
            verify(observer).onChanged(capture())
            assertThat(firstValue).isEqualTo(RunningSession)
        }
    }

    @Test
    fun `init without session should create a one and notify`() = coroutineRule.runBlockingTest {
        val sessionIdRepository = mock<SessionIdRepository> {
            onBlocking { obtainCurrentOrThrow(any()) } doThrow RuntimeException()
        }
        val viewModel = MenuViewModel(sessionIdRepository, mock(), mock())
        val observer = mock<Observer<SessionState>>()

        viewModel.sessionState.observeForever(observer)

        argumentCaptor<SessionState> {
            verify(observer).onChanged(capture())
            assertThat(firstValue).isEqualTo(NoSession)
        }
    }

    @Test
    fun `create should create a session and notify`() = coroutineRule.runBlockingTest {
        val sessionIdRepository = mock<SessionIdRepository> {
            onBlocking { obtainCurrentOrThrow(any()) } doReturn mock()
        }
        val sessionId = mock<SessionId>()
        val sessionRepository = mock<SessionRepository> {
            onBlocking { create() } doReturn sessionId
        }
        val gameId = mock<GameId>()
        val gameRepository = mock<GameRepository> { onBlocking { create() } doReturn gameId }
        val viewModel = MenuViewModel(sessionIdRepository, sessionRepository, gameRepository)
        val observer = mock<Observer<SessionState>>()

        viewModel.create().observeForever(observer)

        argumentCaptor<SessionState> {
            verify(observer).onChanged(capture())
            assertThat(firstValue).isEqualTo(NewSession(sessionId, gameId))
        }
        verify(sessionIdRepository).registerCurrent(sessionId)
    }
}