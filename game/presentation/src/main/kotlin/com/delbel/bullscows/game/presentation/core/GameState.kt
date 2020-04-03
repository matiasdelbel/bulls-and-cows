package com.delbel.bullscows.game.presentation.core

import com.delbel.bullscows.game.domain.GameId

internal sealed class GameState

internal data class GameWon(private val gameId: GameId) : GameState()

internal data class GameOver(private val gameId: GameId) : GameState()

internal object GameInProgress : GameState()

internal object MalformedGuessError : GameState()

internal object UnrecoverableError : GameState()