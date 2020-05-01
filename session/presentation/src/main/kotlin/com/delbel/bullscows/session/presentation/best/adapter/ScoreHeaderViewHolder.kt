package com.delbel.bullscows.session.presentation.best.adapter

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.delbel.bullscows.session.presentation.R
import com.delbel.bullscows.session.presentation.databinding.ItemBestScoreHeaderBinding

internal class ScoreHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {

        fun id() = R.layout.item_best_score_header

        fun createFrom(parent: ViewGroup): ScoreHeaderViewHolder {
            val view = ItemBestScoreHeaderBinding.inflate(from(parent.context), parent, false)
            return ScoreHeaderViewHolder(view.root)
        }
    }
}
