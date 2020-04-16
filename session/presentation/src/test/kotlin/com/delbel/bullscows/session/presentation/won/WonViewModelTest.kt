package com.delbel.bullscows.session.presentation.won

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.session.domain.SessionId
import com.delbel.bullscows.session.domain.repository.CurrentSessionRepository
import com.delbel.bullscows.session.domain.repository.SessionRepository
import com.delbel.bullscows.session.presentation.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    fun `game should obtain id and post it`() = coroutineRule.runBlockingTest {
        val savedState = mock<SavedStateHandle> { on { get<String>("game_id") } doReturn "123" }
        val sessionIdRepository = mock<CurrentSessionRepository> {
            onBlocking { obtainSessionIdOrCreate(creator = any()) } doReturn SessionId(value = 45)
        }
        val sessionRepository = mock<SessionRepository>()
        val game = mock<Game>()
        val gameRepository = mock<GameRepository> {
            onBlocking { obtainGameBy(id = GameId(id = 123)) } doReturn game
        }
        val viewModel = WonViewModel(savedState, sessionIdRepository, sessionRepository, gameRepository)
        val observer = mock<Observer<Game>>()

        viewModel.game.observeForever(observer)

        argumentCaptor<Game> {
            verify(observer).onChanged(capture())
            assertThat(firstValue).isEqualTo(game)
        }
        verify(sessionRepository).addGameWon(id = SessionId(45), game = game)
    }
}