package com.delbel.bullscows.session.presentation.best

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.delbel.bullscows.session.domain.repository.SessionRepository
import javax.inject.Inject

internal class BestScoresViewModel @Inject constructor(repository: SessionRepository) : ViewModel() {

    val listing = repository.obtainAll().asLiveData()
}