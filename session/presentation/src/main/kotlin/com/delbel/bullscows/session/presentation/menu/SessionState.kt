package com.delbel.bullscows.session.presentation.menu

import com.delbel.bullscows.game.domain.GameId

internal sealed class SessionState {

    data class SessionWithGameInProgress(val gameId: GameId) : SessionState()

    object SessionOver : SessionState()

    data class ResumingGame(val gameId: GameId) : SessionState()
}