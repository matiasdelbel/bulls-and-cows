package com.delbel.bullscows.game.presentation.core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.delbel.bullscows.game.domain.Shift
import com.delbel.bullscows.game.presentation.databinding.GameScreenBoardBinding
import com.delbel.dagger.viewmodel.savedstate.ext.viewModels
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class GameScreen : Fragment() {

    @Inject
    internal lateinit var factory: GameViewModel.Factory
    private val viewModel: GameViewModel by viewModels { factory }

    private var _viewBinding: GameScreenBoardBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val adapter = ShiftsAdapter()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedState: Bundle?
    ): View {
        _viewBinding = GameScreenBoardBinding.inflate(inflater, container, false)
        setUpViewInitialState()

        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments = bundleOf("game_id" to "id") // TODO hardcoded
        viewModel.shifts.observe(viewLifecycleOwner, Observer(::handleShiftsUpdates))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun setUpViewInitialState() {
        (activity as? AppCompatActivity)?.setSupportActionBar(viewBinding.toolbar)
        viewBinding.shifts.adapter = adapter
    }

    private fun handleShiftsUpdates(shiftsUpdate: List<Shift>) {
        adapter.submitList(shiftsUpdate)
    }
}