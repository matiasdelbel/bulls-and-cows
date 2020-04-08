package com.delbel.bullscows.session.domain

data class Session(
    val id: SessionId = SessionId(),
    val guessed: Int = 0,
    val points: Int = 0
)

inline class SessionId(val value: Long = Long.MAX_VALUE)