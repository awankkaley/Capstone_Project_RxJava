package id.awankkaley.capstoneprojectrx.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import id.awankkaley.core.data.Resource
import id.awankkaley.core.domain.model.Popular
import id.awankkaley.core.domain.usecase.PopularUseCase


class SearchViewModel(private val popularUseCase: PopularUseCase) : ViewModel() {

    fun searchResult(query: String): LiveData<Resource<List<Popular>>> {
        return LiveDataReactiveStreams.fromPublisher(popularUseCase.searchMovies(query))
    }

}
