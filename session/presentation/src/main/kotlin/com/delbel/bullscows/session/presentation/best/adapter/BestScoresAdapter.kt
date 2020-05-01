package com.delbel.bullscows.session.presentation.best.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.delbel.bullscows.session.domain.Session

internal class BestScoresAdapter : ListAdapter<Session, ScoreViewHolder>(SessionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ScoreViewHolder.createFrom(parent)

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) =
        holder.bind(session = getItem(position))

    override fun getItemViewType(position: Int) = ScoreViewHolder.id()

    private object SessionDiffCallback : DiffUtil.ItemCallback<Session>() {

        override fun areItemsTheSame(oldItem: Session, newItem: Session) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Session, newItem: Session) = oldItem == newItem
    }
}