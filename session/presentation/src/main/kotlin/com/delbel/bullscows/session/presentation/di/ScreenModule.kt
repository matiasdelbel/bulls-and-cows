package com.delbel.bullscows.session.presentation.di

import com.delbel.bullscows.session.presentation.won.WonScreen
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal interface ScreenModule {

    @ContributesAndroidInjector
    fun contributeWonScreen(): WonScreen
}