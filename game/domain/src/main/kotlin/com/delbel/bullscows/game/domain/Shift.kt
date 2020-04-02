package com.delbel.bullscows.game.domain

import com.delbel.bullscows.game.domain.core.Answer

data class Shift(
    private val attempt: Int = 1,
    private val answer: Answer,
    private val maxAttempts: Int
) {

    fun next(answer: Answer): Shift {
        check(attempt < maxAttempts) { "Game has already ended." }
        return copy(attempt = attempt + 1, answer = answer)
    }

    fun executeIf(gameWon: () -> Unit, gameOver: () -> Unit): Unit = TODO()
}