package com.delbel.bullscows.session.domain

import com.delbel.bullscows.game.domain.Game

data class Session(
    val id: SessionId = SessionId(),
    var current: Game,
    private val won: List<Game> = emptyList()
)

inline class SessionId(val value: Long = Long.MAX_VALUE)