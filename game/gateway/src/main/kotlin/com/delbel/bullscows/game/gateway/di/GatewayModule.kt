package com.delbel.bullscows.game.gateway.di

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.game.gateway.LocalGameRepository
import com.delbel.bullscows.game.gateway.factory.GameFactory
import dagger.Binds
import dagger.Module

@Module(includes = [DatabaseModule::class])
abstract class GatewayModule {

    @Binds
    internal abstract fun bindRepository(repository: LocalGameRepository): GameRepository

    @Binds
    internal abstract fun bindGameFactory(factory: GameFactory): Game.Factory
}