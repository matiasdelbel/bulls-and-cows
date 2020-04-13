package com.delbel.bullscows.session.gateway.di

import com.delbel.bullscows.session.domain.CurrentSession
import com.delbel.bullscows.session.domain.repository.SessionRepository
import com.delbel.bullscows.session.gateway.LocalCurrentSession
import com.delbel.bullscows.session.gateway.LocalSessionRepository
import dagger.Binds
import dagger.Module

@Module(includes = [DatabaseModule::class, PreferencesModule::class])
abstract class GatewayModule {

    @Binds
    internal abstract fun bindRepository(repository: LocalSessionRepository): SessionRepository

    @Binds
    internal abstract fun bindCurrentSession(session: LocalCurrentSession): CurrentSession
}