package com.delbel.bullscows.session.gateway

import android.content.SharedPreferences
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.session.domain.SessionId
import com.delbel.bullscows.session.domain.repository.CurrentSessionRepository
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

    override suspend fun obtainSessionIdOrCreate(creator: suspend () -> SessionId): SessionId {
        val sessionId = obtainSessionId()
        if (sessionId.value == MIN_VALUE) update(CURRENT_SESSION_ID, value = creator().value)

        return obtainSessionId()
    }

    override suspend fun obtainSessionIdOrThrow(exception: Exception): SessionId {
        val sessionId = obtainSessionId()
        if (sessionId.value == MIN_VALUE) throw exception

        return sessionId
    }

    override fun removeSessionId() = update(CURRENT_SESSION_ID, MIN_VALUE)

    private fun obtainSessionId() = SessionId(value = preferences.getLong(CURRENT_SESSION_ID, MIN_VALUE))

    private fun update(key: String, value: Long) = preferences.edit().putLong(key, value).apply()
}