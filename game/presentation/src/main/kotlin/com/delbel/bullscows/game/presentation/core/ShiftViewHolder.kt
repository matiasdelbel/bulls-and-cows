package com.delbel.bullscows.game.presentation.core

import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.delbel.bullscows.game.domain.Shift
import com.delbel.bullscows.game.presentation.R
import com.delbel.bullscows.game.presentation.databinding.ItemShiftBinding

internal class ShiftViewHolder(private val binding: ItemShiftBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {

        fun createFrom(parent: ViewGroup): ShiftViewHolder {
            val view = from(parent.context).inflate(R.layout.item_shift, parent, false)
            val binding = ItemShiftBinding.bind(view)

            return ShiftViewHolder(binding)
        }
    }

    fun bind(shift: Shift) {
        val guess = shift.guess
        binding.firstNumber.text = "${guess.first}"
        binding.secondNumber.text = "${guess.second}"
        binding.thirdNumber.text = "${guess.third}"
        binding.fourthNumber.text = "${guess.fourth}"

        val answer = shift.answer
        binding.bulls.text = "${answer.bulls}"
        binding.cows.text = "${answer.cows}"
    }
}