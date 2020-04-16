package com.delbel.bullscows.session.presentation.menu

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.session.presentation.R
import com.delbel.bullscows.session.presentation.databinding.ScreenMenuBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class MenuScreen : Fragment(R.layout.screen_menu) {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    private val viewModel: MenuViewModel by viewModels { factory }

    private var _viewBinding: ScreenMenuBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        _viewBinding = ScreenMenuBinding.bind(requireView())

        setUpContinueActionVisibility()
        setUpContinueAction()

        setUpCreateAction()
        setUpBestScoresAction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun setUpContinueActionVisibility() {
        viewModel.sessionState.observe(viewLifecycleOwner, Observer {
            viewBinding.continueGame.isVisible = it !is NoSession
        })
    }

    private fun setUpContinueAction() = viewBinding.continueGame.setOnClickListener {
        viewModel.continueGame().observe(viewLifecycleOwner, Observer(::navigateToGameScreen))
    }

    private fun setUpCreateAction() = viewBinding.newGame.setOnClickListener {
        viewModel.create().observe(viewLifecycleOwner, Observer { navigateToGameScreen(it.gameId) })
    }

    private fun setUpBestScoresAction() = viewBinding.bestScores.setOnClickListener {
        TODO()
    }

    private fun navigateToGameScreen(gameId: GameId) {
        val deepLink = Uri.parse(getString(R.string.game_deep_link, gameId.id))
        findNavController().navigate(deepLink)
    }
}