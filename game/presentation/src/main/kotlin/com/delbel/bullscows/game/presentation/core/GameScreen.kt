package com.delbel.bullscows.game.presentation.core

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.delbel.bullscows.game.domain.Shift
import com.delbel.bullscows.game.domain.core.Secret
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.game.presentation.R
import com.delbel.bullscows.game.presentation.databinding.GameScreenBoardBinding
import com.delbel.dagger.viewmodel.savedstate.ext.viewModels
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GameScreen : Fragment(R.layout.game_screen_board) {

    // TODO remove this! Hardcoded until we pass the actually id from other screen
    @Inject
    lateinit var repository: GameRepository

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

        // TODO remove this! Hardcoded until we pass the actually id from other screen
        runBlocking {
            val id = repository.saveGame(Secret(1, 2, 3, 4), maxAttempts = 7)
            arguments = bundleOf("game_id" to id.id)
        }

        viewModel.shifts.observe(viewLifecycleOwner, Observer(::handleShiftsUpdates))
        viewModel.state.observe(viewLifecycleOwner, Observer(::handleGameStateUpdates))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun setUpViewInitialState() {
        (activity as? AppCompatActivity)?.setSupportActionBar(viewBinding.toolbar)
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
        is GameWon -> { }
        is GameOver -> { }
        is GameInProgress -> { }
        is MalformedGuessError -> { }
        is UnrecoverableError -> { }
    }
}