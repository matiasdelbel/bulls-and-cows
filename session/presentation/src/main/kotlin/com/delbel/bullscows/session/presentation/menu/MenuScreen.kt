package com.delbel.bullscows.session.presentation.menu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.session.domain.Session
import com.delbel.bullscows.session.presentation.databinding.ScreenMenuBinding
import com.delbel.bullscows.session.presentation.menu.SessionState.*
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class MenuScreen : Fragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MenuViewModel by viewModels { viewModelFactory }

    private var _viewBinding: ScreenMenuBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedState: Bundle?
    ): View {
        _viewBinding = ScreenMenuBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResumingGame -> navigateToGame(id = it.gameId)

                is SessionWithGameInProgress -> showResumeGameAction(gameId = it.gameId)
                is SessionOver -> viewBinding.resumeGame.isVisible = false
            }
        })

        viewBinding.createGame.setOnClickListener { viewModel.createSession() }
        viewBinding.bestScores.setOnClickListener { TODO("navigate to best scores") }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun showResumeGameAction(gameId: GameId) {
        viewBinding.resumeGame.isVisible = true
        viewBinding.resumeGame.setOnClickListener { navigateToGame(id = gameId) }
    }

    private fun navigateToGame(id: GameId): Unit = TODO("Navigate to Game Board with $id")
}