package id.awankkaley.core.domain.usecase

import id.awankkaley.core.data.Resource
import id.awankkaley.core.data.remote.response.PopularItem
import id.awankkaley.core.domain.model.Popular
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow

interface PopularUseCase {
    fun getAllPopular(): Flowable<Resource<List<Popular>>>
    fun searchMovies(query: String): Flowable<Resource<List<Popular>>>
    fun getFavoritePopular(): Flowable<List<Popular>>
    fun setFavoritePopular(popular: Popular, state: Boolean)
    fun isFavorite(id: String): Flowable<Boolean>
}