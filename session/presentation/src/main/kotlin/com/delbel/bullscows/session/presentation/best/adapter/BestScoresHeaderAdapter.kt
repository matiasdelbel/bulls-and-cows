package com.delbel.bullscows.session.presentation.best.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

internal class BestScoresHeaderAdapter : RecyclerView.Adapter<ScoreHeaderViewHolder>() {

    override fun getItemCount(): Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ScoreHeaderViewHolder.createFrom(parent)

    override fun onBindViewHolder(holder: ScoreHeaderViewHolder, position: Int) {}

    override fun getItemViewType(position: Int) = ScoreHeaderViewHolder.id()
}