package com.delbel.bullscows.session.gateway.di

import com.delbel.bullscows.session.domain.repository.SessionRepository
import com.delbel.bullscows.session.gateway.LocalSessionRepository
import dagger.Binds
import dagger.Module

@Module(includes = [DatabaseModule::class])
abstract class GatewayModule {

    @Binds
    internal abstract fun bindRepository(repository: LocalSessionRepository): SessionRepository
}