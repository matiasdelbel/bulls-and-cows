package com.delbel.bullscows.game.gateway.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import com.delbel.bullscows.game.domain.Shift
import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess

@Entity(
    tableName = "shifts",
    primaryKeys = ["gameId", "attempt"],
    foreignKeys = [ForeignKey(
        entity = GameDo::class,
        parentColumns = ["id"],
        childColumns = ["gameId"],
        onDelete = CASCADE
    )]
)
internal data class ShiftDo(
    val gameId: Long,
    val attempt: Int,
    val bulls: Int,
    val cows: Int,
    val maxAttempts: Int,
    @Embedded val guess: GuessDo
) {

    fun asModel(): Shift = Shift(
        attempt = attempt,
        guess = Guess(guess.first, guess.second, guess.third, guess.fourth),
        answer = Answer(bulls = bulls, cows = cows),
        maxAttempts = maxAttempts
    )

    data class GuessDo(val first: Int, val second: Int, val third: Int, val fourth: Int)
}