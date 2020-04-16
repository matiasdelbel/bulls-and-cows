package com.delbel.bullscows.session.presentation.lost

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.session.domain.repository.CurrentSessionRepository
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
class LostViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun `game should obtain id and post it`() = coroutineRule.runBlockingTest {
        val savedState = mock<SavedStateHandle> { on { get<String>("game_id") } doReturn "123" }
        val sessionIdRepository = mock<CurrentSessionRepository>()
        val game = mock<Game>()
        val gameRepository = mock<GameRepository> {
            onBlocking { obtainGameBy(id = GameId(id = 123)) } doReturn game
        }
        val viewModel = LostViewModel(savedState, sessionIdRepository, gameRepository)
        val observer = mock<Observer<Game>>()

        viewModel.game.observeForever(observer)

        argumentCaptor<Game> {
            verify(observer).onChanged(capture())
            assertThat(firstValue).isEqualTo(game)
        }
        verify(sessionIdRepository).removeSessionId()
    }
}