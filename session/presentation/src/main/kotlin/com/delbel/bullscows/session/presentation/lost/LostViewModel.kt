package com.delbel.bullscows.session.presentation.lost

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.session.domain.repository.CurrentSessionRepository
import com.delbel.bullscows.session.presentation.di.AssistedViewModelFactory
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

internal class LostViewModel @AssistedInject constructor(
    @Assisted handle: SavedStateHandle,
    private val currentSessionRepository: CurrentSessionRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedViewModelFactory<LostViewModel>

    private val gameId = GameId(id = handle.get<String>("game_id")!!.toLong())

    val game = liveData {
        val game = gameRepository.obtainGameBy(id = gameId)
        currentSessionRepository.removeSessionId()

        emit(game)
    }
}