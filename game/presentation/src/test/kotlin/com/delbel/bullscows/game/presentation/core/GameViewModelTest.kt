package com.delbel.bullscows.game.presentation.core

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.Shift
import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.game.presentation.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GameViewModelTest {

    private companion object {
        private const val GAME_ID_KEY = "game_id"
        private const val GAME_ID_MOCK = "id"
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test(expected = NullPointerException::class)
    fun `create with id not on saved state should throw exception`() {
        val handle = savedStateHandle(gameIdError = NullPointerException())
        GameViewModel(handle, repository = mock())
    }

    @Test
    fun `guess with malformed guess should notify error`() = coroutineRule.runBlockingTest {
        val viewModel = GameViewModel(handle = savedStateHandle(), repository = mock())

        viewModel.guess(first = 1, second = 1, third = 3, fourth = 4)

        viewModel.state.assertEqualTo(expected = MalformedGuessError)
    }

    @Test
    fun `guess with game not on repository should notify error`() = coroutineRule.runBlockingTest {
        val repository = mock<GameRepository> {
            onBlocking { game(id = GameId(GAME_ID_MOCK)) } doThrow RuntimeException()
        }
        val viewModel = GameViewModel(savedStateHandle(gameId = GAME_ID_MOCK), repository)

        viewModel.guess(first = 1, second = 2, third = 3, fourth = 4)

        viewModel.state.assertEqualTo(expected = UnrecoverableError)
    }

    @Test
    fun `guess with guessing exception should notify error`() = coroutineRule.runBlockingTest {
        val guess = Guess(first = 1, second = 2, third = 3, fourth = 4)
        val game = mock<Game> { on { guess(guess) } doThrow RuntimeException() }
        val repository = mock<GameRepository> {
            onBlocking { game(id = GameId(GAME_ID_MOCK)) } doReturn game
        }
        val viewModel = GameViewModel(savedStateHandle(gameId = GAME_ID_MOCK), repository)

        viewModel.guess(first = 1, second = 2, third = 3, fourth = 4)

        viewModel.state.assertEqualTo(expected = UnrecoverableError)
    }

    @Test
    fun `guess with shifts update error should notify error`() = coroutineRule.runBlockingTest {
        val gameId = GameId(GAME_ID_MOCK)
        val shift = mock<Shift>()
        val guess = Guess(first = 1, second = 2, third = 3, fourth = 4)
        val game = mock<Game> { on { guess(guess) } doReturn shift }
        val repository = mock<GameRepository> {
            onBlocking { game(id = gameId) } doReturn game
            onBlocking { update(id = gameId, shift = shift) } doThrow RuntimeException()
        }
        val viewModel = GameViewModel(savedStateHandle(gameId = GAME_ID_MOCK), repository)

        viewModel.guess(first = 1, second = 2, third = 3, fourth = 4)

        viewModel.state.assertEqualTo(expected = UnrecoverableError)
    }

    @Test
    fun `guess with game won should notify game won`() = coroutineRule.runBlockingTest {
        val gameId = GameId(GAME_ID_MOCK)
        val shift = Shift(attempt = 1, guess = mock(), answer = Answer(bulls = 4, cows = 0), maxAttempts = 2)
        val guess = Guess(first = 1, second = 2, third = 3, fourth = 4)
        val game = mock<Game> { on { guess(guess) } doReturn shift }
        val repository = mock<GameRepository> {
            onBlocking { game(id = gameId) } doReturn game
            onBlocking { update(id = gameId, shift = shift) } doReturn Unit
        }
        val viewModel = GameViewModel(savedStateHandle(gameId = GAME_ID_MOCK), repository)

        viewModel.guess(first = 1, second = 2, third = 3, fourth = 4)

        viewModel.state.assertEqualTo(expected = GameWon(gameId = gameId))
    }

    @Test
    fun `guess with game over should notify game over`() = coroutineRule.runBlockingTest {
        val gameId = GameId(GAME_ID_MOCK)
        val shift = Shift(attempt = 2, guess = mock(), answer = Answer(bulls = 2, cows = 0), maxAttempts = 2)
        val guess = Guess(first = 1, second = 2, third = 3, fourth = 4)
        val game = mock<Game> { on { guess(guess) } doReturn shift }
        val repository = mock<GameRepository> {
            onBlocking { game(id = gameId) } doReturn game
            onBlocking { update(id = gameId, shift = shift) } doReturn Unit
        }
        val viewModel = GameViewModel(savedStateHandle(gameId = GAME_ID_MOCK), repository)

        viewModel.guess(first = 1, second = 2, third = 3, fourth = 4)

        viewModel.state.assertEqualTo(expected = GameOver(gameId = gameId))
    }

    @Test
    fun `guess with in progress game should notify in progress`() = coroutineRule.runBlockingTest {
        val gameId = GameId(GAME_ID_MOCK)
        val shift = Shift(attempt = 2, guess = mock(), answer = Answer(bulls = 2, cows = 0), maxAttempts = 4)
        val guess = Guess(first = 1, second = 2, third = 3, fourth = 4)
        val game = mock<Game> { on { guess(guess) } doReturn shift }
        val repository = mock<GameRepository> {
            onBlocking { game(id = gameId) } doReturn game
            onBlocking { update(id = gameId, shift = shift) } doReturn Unit
        }
        val viewModel = GameViewModel(savedStateHandle(gameId = GAME_ID_MOCK), repository)

        viewModel.guess(first = 1, second = 2, third = 3, fourth = 4)

        viewModel.state.assertEqualTo(expected = GameInProgress)
    }

    private fun LiveData<GameState>.assertEqualTo(expected: GameState) =
        assertThat(value).isEqualTo(expected)

    private fun savedStateHandle(gameId: String = "some_id") = mock<SavedStateHandle> {
        on { get<String>(GAME_ID_KEY) } doReturn gameId
    }

    private fun savedStateHandle(gameIdError: Throwable) = mock<SavedStateHandle> {
        on { get<String>(GAME_ID_KEY) } doThrow gameIdError
    }
}