package id.awankkaley.favorite

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.awankkaley.core.domain.usecase.PopularUseCase

class FavViewModel(popularUseCase: PopularUseCase) : ViewModel() {
    val favorite = LiveDataReactiveStreams.fromPublisher(popularUseCase.getFavoritePopular())
}