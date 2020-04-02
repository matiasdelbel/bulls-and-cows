package com.delbel.bullscows.game.domain.core

data class Guess(
    private val first: Int,
    private val second: Int,
    private val third: Int,
    private val fourth: Int
) {

    private val guess: List<Int> = listOf(first, second, third, fourth).distinct()

    init {
        require(guess.count() == 4) { "The four-digit guess number must be all different." }
    }

    internal fun guess(first: Int, second: Int, third: Int, fourth: Int): Answer {
        val secret = listOf(first, second, third, fourth)

        val bulls = guess.filterIndexed { index, value -> value == secret[index] }.size
        val cows = guess.intersect(secret).size - bulls

        return Answer(bulls, cows)
    }
}