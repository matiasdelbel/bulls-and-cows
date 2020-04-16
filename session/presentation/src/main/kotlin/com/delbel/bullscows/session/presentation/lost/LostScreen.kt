package com.delbel.bullscows.session.presentation.lost

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.session.presentation.R
import com.delbel.bullscows.session.presentation.databinding.ScreenLostBinding
import com.delbel.dagger.viewmodel.savedstate.ext.viewModels
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class LostScreen : Fragment(R.layout.screen_lost) {

    @Inject
    internal lateinit var factory: LostViewModel.Factory
    private val viewModel: LostViewModel by viewModels { factory }

    private var _viewBinding: ScreenLostBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        _viewBinding = ScreenLostBinding.bind(requireView())
        setUpCContinueAction()

        viewModel.session.observe(viewLifecycleOwner, Observer {
            viewBinding.points.text = getString(R.string.points_on_lost, it.points, it.guessed)
        })

        viewModel.game.observe(viewLifecycleOwner, Observer {
            viewBinding.secret.text = getString(R.string.secret, it.secret.asString())
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun setUpCContinueAction() = viewBinding.next.setOnClickListener {
        viewModel.createSession().observe(viewLifecycleOwner, Observer(::navigateToGameScreen))
    }

    private fun navigateToGameScreen(gameId: GameId) {
        val deepLink = Uri.parse(getString(R.string.game_deep_link, gameId.id))
        val options = NavOptions.Builder().setPopUpTo(R.id.session_graph, true).build()

        findNavController().navigate(deepLink, options)
    }
}