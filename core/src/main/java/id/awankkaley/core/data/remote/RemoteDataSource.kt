package id.awankkaley.core.data.remote

import android.annotation.SuppressLint
import android.util.Log
import id.awankkaley.core.data.remote.network.ApiResponse
import id.awankkaley.core.data.remote.network.ApiService
import id.awankkaley.core.data.remote.response.PopularItem
import id.awankkaley.core.data.remote.response.PopularListResponse
import id.awankkaley.core.util.Util
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {


    @SuppressLint("CheckResult")
    fun getPopular(): Flowable<ApiResponse<List<PopularItem>>> {
        val result = PublishSubject.create<ApiResponse<List<PopularItem>>>()
        val client = apiService.getPopular(Util.API_KEY)
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe({
                val dataArray = it.results
                result.onNext(if (dataArray.isNotEmpty()) ApiResponse.Success(dataArray) else ApiResponse.Empty)
            }, {
                result.onNext(ApiResponse.Error(it.message.toString()))
                Log.e("REMOTEDATASOURCE", it.toString())
            })
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("CheckResult")
    fun searchMovies(query: String?): Flowable<ApiResponse<List<PopularItem>>> {
        val result = PublishSubject.create<ApiResponse<List<PopularItem>>>()
        val client = apiService.searchMovies(Util.API_KEY, query.toString())
        Log.e("REMOTEDATASOURCE", query.toString())
        client.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe({
                val dataArray = it.results
                result.onNext(if (dataArray.isNotEmpty()) ApiResponse.Success(dataArray) else ApiResponse.Empty)
            }, {
                result.onNext(ApiResponse.Error(it.message.toString()))
                Log.e("REMOTEDATASOURCE", it.toString())
            })
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

}

