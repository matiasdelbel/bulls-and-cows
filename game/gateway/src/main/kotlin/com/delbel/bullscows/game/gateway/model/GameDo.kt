package com.delbel.bullscows.game.gateway.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.Shift
import com.delbel.bullscows.game.domain.core.Secret

@Entity(tableName = "games")
internal data class GameDo(val maxAttempts: Int, @Embedded val secret: SecretDo) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = Long.MAX_VALUE

    fun asModel(currentShift: Shift?) = Game(
        id = GameId(id),
        secret = Secret(secret.first, secret.second, secret.third, secret.fourth),
        maxAttempts = maxAttempts,
        current = currentShift
    )

    companion object {

        fun createFrom(game: Game) = GameDo(
            maxAttempts = game.maxAttempts,
            secret = SecretDo.createFrom(game.secret)
        )
    }

    data class SecretDo(val first: Int, val second: Int, val third: Int, val fourth: Int) {

        companion object {

            fun createFrom(secret: Secret) = SecretDo(
                first = secret.first,
                second = secret.second,
                third = secret.third,
                fourth = secret.fourth
            )
        }
    }
}