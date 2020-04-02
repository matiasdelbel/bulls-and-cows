package com.delbel.bullscows.game.domain.core

data class Answer(
    private val bulls: Int,
    private val cows: Int
) {

    init {
        require(bulls in 0..4) { "Bulls ($bulls) should be a number between 0 and 4" }
        require(cows in 0..4) { "Cows ($cows) should be a number between 0 and 4" }
        require(bulls + cows in 0..4) { "The sum of bulls ({$bulls + $cows}) and cows should be a number between 0 and 4" }
    }
}