package id.awankkaley.core.data.remote.network

import id.awankkaley.core.util.Util
import id.awankkaley.core.data.remote.response.PopularListResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(Util.popular)
    fun getPopular(
        @Query("api_key") string: String
    ): Flowable<PopularListResponse>

    @GET(Util.search)
    fun searchMovies(
        @Query("api_key") key: String,
        @Query("query") search: String

    ): Flowable<PopularListResponse>

}
