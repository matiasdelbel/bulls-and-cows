package com.delbel.bullscows.game.presentation.di

import dagger.Module

@Module(includes = [AssistedViewModelModule::class, ScreenModule::class])
interface PresentationModule