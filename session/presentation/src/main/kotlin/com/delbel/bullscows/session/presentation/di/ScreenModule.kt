package com.delbel.bullscows.session.presentation.di

import com.delbel.bullscows.session.presentation.best.BestScoreScreen
import com.delbel.bullscows.session.presentation.lost.LostScreen
import com.delbel.bullscows.session.presentation.menu.MenuScreen
import com.delbel.bullscows.session.presentation.won.WonScreen
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal interface ScreenModule {

    @ContributesAndroidInjector
    fun contributeMenuScreen(): MenuScreen

    @ContributesAndroidInjector
    fun contributeWonScreen(): WonScreen

    @ContributesAndroidInjector
    fun contributeLostScreen(): LostScreen

    @ContributesAndroidInjector
    fun contributeBestScoreScreen(): BestScoreScreen
}