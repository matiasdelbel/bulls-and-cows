package com.delbel.bullscows.game.presentation.core

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.delbel.bullscows.game.domain.Shift
import com.delbel.bullscows.game.presentation.R
import com.delbel.bullscows.game.presentation.databinding.GameScreenBoardBinding
import com.delbel.dagger.viewmodel.savedstate.ext.viewModels
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class GameScreen : Fragment(R.layout.game_screen_board) {

    @Inject
    internal lateinit var factory: GameViewModel.Factory
    private val viewModel: GameViewModel by viewModels { factory }

    private var _viewBinding: GameScreenBoardBinding? = null
    private val viewBinding get() = _viewBinding!!

    @Inject
    internal lateinit var valueWrapper: GuessValueWrapper

    private val adapter = ShiftsAdapter()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        _viewBinding = GameScreenBoardBinding.bind(requireView())
        setUpViewInitialState()

        viewModel.shifts.observe(viewLifecycleOwner, Observer(::handleShiftsUpdates))
        viewModel.state.observe(viewLifecycleOwner, Observer(::handleGameStateUpdates))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun setUpViewInitialState() {
        viewBinding.shifts.adapter = adapter

        val header = viewBinding.header
        with(header.firstNumber) { setOnClickListener { valueWrapper.wrapNext(button = this) } }
        with(header.secondNumber) { setOnClickListener { valueWrapper.wrapNext(button = this) } }
        with(header.thirdNumber) { setOnClickListener { valueWrapper.wrapNext(button = this) } }
        with(header.fourthNumber) { setOnClickListener { valueWrapper.wrapNext(button = this) } }

        header.guess.setOnClickListener {
            viewModel.guess(
                first = valueWrapper.unwrap(button = header.firstNumber),
                second = valueWrapper.unwrap(button = header.secondNumber),
                third = valueWrapper.unwrap(button = header.thirdNumber),
                fourth = valueWrapper.unwrap(button = header.fourthNumber)
            )
        }
    }

    private fun handleShiftsUpdates(shiftsUpdate: List<Shift>) {
        adapter.submitList(shiftsUpdate)
    }

    // TODO
    private fun handleGameStateUpdates(gameState: GameState) = when (gameState) {
        is GameWon -> navigateToGameWonScreen(gameState)
        is GameOver -> navigateToGameOverScreen(gameState)
        is GameInProgress -> { }
        is MalformedGuessError -> { }
        is UnrecoverableError -> { }
    }

    private fun navigateToGameWonScreen(gameWon: GameWon) {
        val deepLink = Uri.parse(getString(R.string.game_won_deep_link, gameWon.gameId.id))
        val options = NavOptions.Builder().setPopUpTo(R.id.game_graph, true).build()

        findNavController().navigate(deepLink, options)
    }

    private fun navigateToGameOverScreen(gameOver: GameOver) {
        val deepLink = Uri.parse(getString(R.string.game_over_deep_link, gameOver.gameId.id))
        val options = NavOptions.Builder().setPopUpTo(R.id.game_graph, true).build()

        findNavController().navigate(deepLink, options)
    }
}