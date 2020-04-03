package com.delbel.bullscows.game.presentation.core

import com.delbel.bullscows.game.domain.GameId

sealed class GameState

data class GameWon(private val gameId: GameId) : GameState()

data class GameOver(private val gameId: GameId) : GameState()

object GameInProgress : GameState()

object MalformedGuessError : GameState()

object UnrecoverableError : GameState()