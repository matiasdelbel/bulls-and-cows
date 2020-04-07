package com.delbel.bullscows.session.presentation.di

import dagger.Module

@Module(includes = [ViewModelModule::class, ScreenModule::class])
interface PresentationModule