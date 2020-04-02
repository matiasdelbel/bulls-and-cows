package com.delbel.bullscows.game.domain.core

data class Secret(
    private val first: Int,
    private val second: Int,
    private val third: Int,
    private val fourth: Int
) {

    init {
        val secret = listOf(first, second, third, fourth).distinct()
        require(secret.count() == 4) { "The four-digit secret number must be all different." }
    }

    fun guess(guess: Guess) = guess.guess(first, second, third, fourth)
}