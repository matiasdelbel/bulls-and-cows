package com.delbel.bullscows.session.presentation.best

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.MergeAdapter
import com.delbel.bullscows.session.domain.Session
import com.delbel.bullscows.session.presentation.R
import com.delbel.bullscows.session.presentation.best.adapter.BestScoresAdapter
import com.delbel.bullscows.session.presentation.best.adapter.BestScoresHeaderAdapter
import com.delbel.bullscows.session.presentation.best.adapter.MarginItemDecoration
import com.delbel.bullscows.session.presentation.databinding.ScreenBestScoresBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class BestScoreScreen : Fragment(R.layout.screen_best_scores) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<BestScoresViewModel> { viewModelFactory }

    private var _viewBinding: ScreenBestScoresBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val headerAdapter = BestScoresHeaderAdapter()
    private val adapter = BestScoresAdapter()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        _viewBinding = ScreenBestScoresBinding.bind(requireView())
        setUpViewInitialState()

        viewModel.listing.observe(viewLifecycleOwner, Observer(::handleBestScores))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun setUpViewInitialState() {
        viewBinding.list.adapter = MergeAdapter(headerAdapter, adapter)
        viewBinding.list.addItemDecoration(MarginItemDecoration())
    }

    private fun handleBestScores(page: List<Session>) = adapter.submitList(page)
}