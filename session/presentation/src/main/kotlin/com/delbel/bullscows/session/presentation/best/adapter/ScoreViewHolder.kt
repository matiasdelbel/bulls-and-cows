package com.delbel.bullscows.session.presentation.best.adapter

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.delbel.bullscows.session.domain.Session
import com.delbel.bullscows.session.presentation.R
import com.delbel.bullscows.session.presentation.databinding.ItemBestScoreBinding

internal class ScoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {

        fun id() = R.layout.item_best_score

        fun createFrom(parent: ViewGroup): ScoreViewHolder {
            val view = ItemBestScoreBinding.inflate(from(parent.context), parent, false)
            return ScoreViewHolder(view.root)
        }
    }

    private val viewBinding = ItemBestScoreBinding.bind(itemView)

    fun bind(session: Session) {
        val position = bindingAdapterPosition + 1
        viewBinding.position.text = "$position"

        viewBinding.points.text = "${session.points}"
        viewBinding.guesses.text = "${session.guessed}"
    }
}
