package com.delbel.bullscows.game.presentation.di

import com.delbel.bullscows.game.presentation.core.GameScreen
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal interface ScreenModule {

    @ContributesAndroidInjector
    fun contributeGameScreen(): GameScreen
}