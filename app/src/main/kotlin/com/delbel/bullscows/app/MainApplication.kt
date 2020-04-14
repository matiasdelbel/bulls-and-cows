package com.delbel.bullscows.app

import android.app.Application
import com.delbel.bullscows.app.di.DaggerMainComponent
import com.delbel.bullscows.session.domain.CurrentSession
import com.delbel.bullscows.session.domain.repository.SessionRepository
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainApplication : Application(), HasAndroidInjector {

    // TODO remove this! Hardcoded until we create the session
    @Inject
    lateinit var repository: SessionRepository
    @Inject
    lateinit var currentSession: CurrentSession

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        injectDependencies()

        createSession()
    }

    override fun androidInjector() = androidInjector

    private fun injectDependencies() = DaggerMainComponent.builder()
        .application(application = this)
        .build()
        .inject(application = this)

    // TODO remove this! Hardcoded until we create the session
    private fun createSession() = runBlocking {
        val sessionId = repository.create()
        currentSession.update(sessionId)
    }
}