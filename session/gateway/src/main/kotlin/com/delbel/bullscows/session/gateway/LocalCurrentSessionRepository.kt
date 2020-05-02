package com.delbel.bullscows.session.gateway

import android.content.SharedPreferences
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.session.domain.SessionId
import com.delbel.bullscows.session.domain.repository.CurrentSessionRepository
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import java.lang.Long.MIN_VALUE
import javax.inject.Inject

internal class LocalCurrentSessionRepository @Inject constructor(
    private val preferences: SharedPreferences
) : CurrentSessionRepository {

    companion object {
        private const val CURRENT_SESSION_ID = "CURRENT_SESSION_ID"
        private const val CURRENT_GAME_ID = "CURRENT_GAME_ID"
    }

    private val sessionChannel = ConflatedBroadcastChannel<SessionId?>()
    private val gameChannel = ConflatedBroadcastChannel<GameId?>()

    private val preferencesCallback = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == CURRENT_SESSION_ID)
            publish(key = CURRENT_SESSION_ID, channel = sessionChannel) { SessionId(value = it) }

        if (key == CURRENT_GAME_ID)
            publish(key = CURRENT_GAME_ID, channel = gameChannel) { GameId(id = it) }
    }

    init {
        preferences.registerOnSharedPreferenceChangeListener(preferencesCallback)
    }

    override fun register(sessionId: SessionId, gameId: GameId) {
        update(CURRENT_SESSION_ID, value = sessionId.value)
        update(CURRENT_GAME_ID, value = gameId.id)
    }

    override fun updateGameId(gameId: GameId) = update(CURRENT_GAME_ID, value = gameId.id)

    override fun obtainSessionId() = sessionChannel.asFlow()

    override fun obtainGameId() = gameChannel.asFlow()

    override fun clear() {
        update(CURRENT_SESSION_ID, value = MIN_VALUE)
        update(CURRENT_GAME_ID, value = MIN_VALUE)
    }

    private fun <T> publish(key: String, channel: BroadcastChannel<T?>, mapper: (Long) -> T) {
        val value = preferences.getLong(key, MIN_VALUE)

        if (value == MIN_VALUE) channel.offer(element = null)
        else channel.offer(element = mapper(value))
    }

    private fun update(key: String, value: Long) = preferences.edit().putLong(key, value).apply()
}