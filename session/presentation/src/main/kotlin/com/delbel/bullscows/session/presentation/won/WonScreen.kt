package com.delbel.bullscows.session.presentation.won

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.delbel.bullscows.session.presentation.R
import com.delbel.bullscows.session.presentation.databinding.ScreenWonBinding
import com.delbel.dagger.viewmodel.savedstate.ext.viewModels
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class WonScreen : Fragment(R.layout.screen_won) {

    @Inject
    internal lateinit var factory: WonViewModel.Factory
    private val viewModel: WonViewModel by viewModels { factory }

    private var _viewBinding: ScreenWonBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        _viewBinding = ScreenWonBinding.bind(requireView())

        viewModel.game.observe(viewLifecycleOwner, Observer {
            viewBinding.secret.text = getString(R.string.secret, it.secret.asString())
        })

        viewBinding.next.setOnClickListener { TODO() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}