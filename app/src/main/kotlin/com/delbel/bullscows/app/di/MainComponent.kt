package com.delbel.bullscows.app.di

import android.app.Application
import com.delbel.bullscows.app.MainApplication
import com.delbel.bullscows.game.gateway.di.GatewayModule
import com.delbel.bullscows.game.presentation.di.PresentationModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        PresentationModule::class,
        GatewayModule::class]
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