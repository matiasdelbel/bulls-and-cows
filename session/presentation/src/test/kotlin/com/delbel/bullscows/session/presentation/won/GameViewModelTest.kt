package com.delbel.bullscows.session.presentation.won

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.session.domain.CurrentSession
import com.delbel.bullscows.session.domain.SessionId
import com.delbel.bullscows.session.domain.repository.SessionRepository
import com.delbel.bullscows.session.presentation.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GameViewModelTest {

    private companion object {
        private const val GAME_ID_KEY = "game_id"
        private const val GAME_ID_MOCK = 123L
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun `game should obtain id and post it`() = coroutineRule.runBlockingTest {
        val savedState = mock<SavedStateHandle> { on { get<String>(GAME_ID_KEY) } doReturn "123" }
        val session = mock<CurrentSession> { onBlocking { id() } doReturn SessionId(value = 45) }
        val sessionRepository = mock<SessionRepository>()
        val game = mock<Game>()
        val gameRepository = mock<GameRepository> {
            onBlocking { obtainGameBy(id = GameId(id = 123)) } doReturn game
        }
        val viewModel = WonViewModel(savedState, session, sessionRepository, gameRepository)
        val observer = mock<Observer<Game>>()

        viewModel.game.observeForever(observer)

        argumentCaptor<Game> {
            verify(observer).onChanged(capture())
            assertThat(firstValue).isEqualTo(game)
        }
        verify(sessionRepository).addGameWon(id = SessionId(45), game = game)
    }
}