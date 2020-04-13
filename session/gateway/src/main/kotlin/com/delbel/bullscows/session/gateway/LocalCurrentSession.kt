package com.delbel.bullscows.session.gateway

import android.content.SharedPreferences
import com.delbel.bullscows.session.domain.CurrentSession
import com.delbel.bullscows.session.domain.SessionId
import javax.inject.Inject

internal class LocalCurrentSession @Inject constructor(
    private val preferences: SharedPreferences
) : CurrentSession {

    companion object {
        private const val CURRENT_SESSION_ID = "CURRENT_SESSION_ID"
    }

    override suspend fun id(): SessionId {
        val sessionId = preferences.getLong(CURRENT_SESSION_ID, Long.MAX_VALUE)

        return SessionId(value = sessionId)
    }
}