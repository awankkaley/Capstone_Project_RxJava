package id.awankkaley.capstoneprojectrx.home

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import id.awankkaley.core.domain.usecase.PopularUseCase


class HomeViewModel(popularUseCase: PopularUseCase) : ViewModel() {
    val popular = LiveDataReactiveStreams.fromPublisher(popularUseCase.getAllPopular())
}

