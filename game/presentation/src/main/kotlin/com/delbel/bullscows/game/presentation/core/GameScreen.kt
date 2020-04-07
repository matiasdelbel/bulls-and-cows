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
import com.delbel.bullscows.game.domain.core.Secret
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.game.presentation.databinding.GameScreenBoardBinding
import com.delbel.dagger.viewmodel.savedstate.ext.viewModels
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GameScreen : Fragment() {

    // TODO remove this! Hardcoded until we pass the actually id from other screen
    @Inject
    lateinit var repository: GameRepository

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
        // TODO handle guess button

        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO remove this! Hardcoded until we pass the actually id from other screen
        runBlocking {
            val game = repository.create()
            arguments = bundleOf("game_id" to game.id.id)
            // TODO move to the onActivityCreated
            viewModel.shifts.observe(viewLifecycleOwner, Observer(::handleShiftsUpdates))
        }
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