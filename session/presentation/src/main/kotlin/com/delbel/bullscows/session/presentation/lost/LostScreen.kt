package com.delbel.bullscows.session.presentation.lost

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.delbel.bullscows.session.presentation.R
import com.delbel.bullscows.session.presentation.databinding.ScreenLostBinding
import com.delbel.bullscows.session.presentation.databinding.ScreenWonBinding
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