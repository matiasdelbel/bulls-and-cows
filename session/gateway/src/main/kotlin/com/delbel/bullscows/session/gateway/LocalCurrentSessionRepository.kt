package com.delbel.bullscows.session.gateway

import android.content.SharedPreferences
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.session.domain.SessionId
import com.delbel.bullscows.session.domain.repository.CurrentSessionRepository
import com.delbel.bullscows.session.domain.repository.NoGameException
import com.delbel.bullscows.session.domain.repository.NoSessionException
import java.lang.Long.MIN_VALUE
import javax.inject.Inject

internal class LocalCurrentSessionRepository @Inject constructor(
    private val preferences: SharedPreferences
) : CurrentSessionRepository {

    companion object {
        private const val CURRENT_SESSION_ID = "CURRENT_SESSION_ID"
        private const val CURRENT_GAME_ID = "CURRENT_GAME_ID"
    }

    override fun register(sessionId: SessionId, gameId: GameId) {
        update(CURRENT_SESSION_ID, value = sessionId.value)
        update(CURRENT_GAME_ID, value = gameId.id)
    }

    override fun updateGameId(gameId: GameId) = update(CURRENT_GAME_ID, value = gameId.id)

    override fun obtainSessionId(): SessionId =
        SessionId(value = obtainOrThrow(key = CURRENT_SESSION_ID, exception = NoSessionException()))

    override fun obtainGameId() =
        GameId(id = obtainOrThrow(key = CURRENT_GAME_ID, exception = NoGameException()))

    override fun clear() {
        update(CURRENT_SESSION_ID, value = MIN_VALUE)
        update(CURRENT_GAME_ID, value = MIN_VALUE)
    }

    private fun obtainOrThrow(key: String, exception: RuntimeException): Long {
        val value = preferences.getLong(key, MIN_VALUE)
        if (value == MIN_VALUE) throw exception

        return value
    }

    private fun update(key: String, value: Long) = preferences.edit().putLong(key, value).apply()
}