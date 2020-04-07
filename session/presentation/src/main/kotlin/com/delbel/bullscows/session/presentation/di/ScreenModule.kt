package com.delbel.bullscows.session.presentation.di

import com.delbel.bullscows.session.presentation.menu.MenuScreen
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal interface ScreenModule {

    @ContributesAndroidInjector
    fun contributeMenuScreen(): MenuScreen
}