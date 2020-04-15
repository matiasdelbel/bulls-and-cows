package com.delbel.bullscows.session.presentation.menu

import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.session.domain.SessionId

internal sealed class SessionState

internal data class NewSession(val sessionId: SessionId, val gameId: GameId) : SessionState()

internal object RunningSession : SessionState()

internal object NoSession : SessionState()