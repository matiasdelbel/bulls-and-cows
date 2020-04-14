package com.delbel.bullscows.session.presentation.di

import dagger.Module

@Module(includes = [AssistedViewModelModule::class, ScreenModule::class])
interface PresentationModule