package com.delbel.bullscows.app.di

import android.app.Application
import com.delbel.bullscows.app.MainApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton
import com.delbel.bullscows.game.gateway.di.GatewayModule as GameGatewayModule
import com.delbel.bullscows.game.presentation.di.PresentationModule as GamePresentationModule
import com.delbel.bullscows.session.gateway.di.GatewayModule as SessionGatewayModule
import com.delbel.bullscows.session.presentation.di.PresentationModule as SessionPresentationModule

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        GamePresentationModule::class,
        GameGatewayModule::class,
        SessionPresentationModule::class,
        SessionGatewayModule::class
    ]
)
@Singleton
internal interface MainComponent {

    fun inject(application: MainApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): MainComponent
    }
}