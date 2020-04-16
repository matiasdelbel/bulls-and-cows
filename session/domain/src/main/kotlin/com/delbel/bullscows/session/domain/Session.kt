package com.delbel.bullscows.session.domain

import com.delbel.bullscows.game.domain.Game

data class Session(
    val id: SessionId = SessionId(),
    val guessed: Int = 0,
    val points: Int = 0
) {

    operator fun plus(game: Game) = copy(guessed = guessed + 1, points = points + game.points())
}

inline class SessionId(val value: Long = Long.MAX_VALUE)