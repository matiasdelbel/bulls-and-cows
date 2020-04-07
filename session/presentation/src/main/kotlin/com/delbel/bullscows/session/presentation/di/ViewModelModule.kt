package com.delbel.bullscows.session.presentation.di

import androidx.lifecycle.ViewModel
import com.delbel.bullscows.session.presentation.menu.MenuViewModel
import com.delbel.dagger.viewmodel.general.ViewModelKey
import com.delbel.dagger.viewmodel.general.di.ViewModelFactoryModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelFactoryModule::class])
internal interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MenuViewModel::class)
    fun bindMenuViewModel(viewModel: MenuViewModel): ViewModel
}