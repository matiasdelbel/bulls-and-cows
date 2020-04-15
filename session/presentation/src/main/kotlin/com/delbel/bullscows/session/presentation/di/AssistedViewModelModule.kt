package com.delbel.bullscows.session.presentation.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.delbel.dagger.viewmodel.savedstate.ViewModelFactory
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_AssistedViewModelModule::class])
internal interface AssistedViewModelModule

internal interface AssistedViewModelFactory<T : ViewModel> : ViewModelFactory<T> {

    override fun create(handle: SavedStateHandle): T
}