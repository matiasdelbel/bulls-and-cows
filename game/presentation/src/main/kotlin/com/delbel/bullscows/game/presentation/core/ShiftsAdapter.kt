package com.delbel.bullscows.game.presentation.core

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.delbel.bullscows.game.domain.Shift

internal class ShiftsAdapter : ListAdapter<Shift, ShiftViewHolder>(ShiftDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ShiftViewHolder.createFrom(parent)

    override fun onBindViewHolder(holder: ShiftViewHolder, position: Int) =
        holder.bind(shift = getItem(position))

    object ShiftDiffCallback : DiffUtil.ItemCallback<Shift>() {

        override fun areItemsTheSame(oldItem: Shift, newItem: Shift) =
            oldItem.attempt == newItem.attempt

        override fun areContentsTheSame(oldItem: Shift, newItem: Shift) =
            oldItem == newItem
    }
}