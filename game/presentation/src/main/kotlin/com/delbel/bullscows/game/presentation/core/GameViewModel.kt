package com.delbel.bullscows.game.presentation.core

import androidx.lifecycle.*
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.Shift
import com.delbel.bullscows.game.domain.core.Guess
import com.delbel.bullscows.game.domain.core.MalformedGuessException
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.game.presentation.di.AssistedViewModelFactory
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch

internal class GameViewModel @AssistedInject constructor(
    @Assisted handle: SavedStateHandle,
    private val repository: GameRepository
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedViewModelFactory<GameViewModel>

    private val id = GameId(id = handle.get<String>("game_id")!!.toLong())
    private val _state = MutableLiveData<GameState>()

    val state: LiveData<GameState> get() = _state
    val shifts = repository.shiftsFor(id).asLiveData()

    fun guess(first: Int, second: Int, third: Int, fourth: Int) = viewModelScope.launch {
        runCatching { guess(guess = Guess(first, second, third, fourth)) }
            .onSuccess(::handleShift)
            .onFailure(::handleShiftError)
    }

    private suspend fun guess(guess: Guess): Shift {
        val game = repository.obtainGameBy(id)
        val shift = game.guess(guess)
        repository.addShift(gameId = id, shift = shift)

        return shift
    }

    private fun handleShift(shift: Shift) = shift.executeIf(
        won = { _state.value = GameWon(gameId = id) },
        lost = { _state.value = GameOver(gameId = id) },
        inProgress = { _state.value = GameInProgress }
    )

    private fun handleShiftError(error: Throwable): Unit = when (error) {
        is MalformedGuessException -> _state.value = MalformedGuessError
        else -> _state.value = UnrecoverableError
    }
}