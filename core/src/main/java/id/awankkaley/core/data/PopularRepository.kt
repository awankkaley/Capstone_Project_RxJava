package id.awankkaley.core.data


import android.annotation.SuppressLint
import android.util.Log
import id.awankkaley.core.data.local.LocalDataSource
import id.awankkaley.core.data.remote.RemoteDataSource
import id.awankkaley.core.data.remote.network.ApiResponse
import id.awankkaley.core.data.remote.response.PopularItem
import id.awankkaley.core.domain.model.Popular
import id.awankkaley.core.domain.repository.IPopularRepository
import id.awankkaley.core.utils.AppExecutors
import id.awankkaley.core.utils.DataMapper
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

class PopularRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IPopularRepository {


    override fun getAllPopular(): Flowable<Resource<List<Popular>>> =
        object : NetworkBoundResource<List<Popular>, List<PopularItem>>() {
            override fun loadFromDB(): Flowable<List<Popular>> {
                return localDataSource.getAllPopular().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Popular>?): Boolean =
                data == null || data.isEmpty()

            override fun createCall(): Flowable<ApiResponse<List<PopularItem>>> =
                remoteDataSource.getPopular()

            override fun saveCallResult(data: List<PopularItem>) {
                val popularList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertPopular(popularList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()

            }
        }.asFlowable()

    private val result = PublishSubject.create<Resource<List<Popular>>>()
    private val mCompositeDisposable = CompositeDisposable()

    @SuppressLint("CheckResult")
    override fun searchMovies(query: String): Flowable<Resource<List<Popular>>> {
        val apiResponse = remoteDataSource.searchMovies(query)
        result.onNext(Resource.Loading())
        val response = apiResponse.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(5,TimeUnit.SECONDS)
            .subscribe { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        val data = DataMapper.mapResponseToDomain(response.data)
                        result.onNext(Resource.Success(data))
                    }
                    is ApiResponse.Empty -> {
                        result.onNext(Resource.Empty())
                    }
                    is ApiResponse.Error -> {
                        result.onNext(Resource.Error(response.errorMessage))
                    }
                    else -> {
                        Log.e("Popular Repository", "No Publishing Data")
                    }
                }
            }
        mCompositeDisposable.add(response)

        return result.toFlowable(BackpressureStrategy.BUFFER)

    }

    override fun getFavoritePopular(): Flowable<List<Popular>> {
        return localDataSource.getFavoritePopular().map {
            DataMapper.mapEntitiesToDomain(it)
        }


    }

    private val result2 = PublishSubject.create<Boolean>()
    private val mCompositeDisposable2 = CompositeDisposable()
    override fun isFavorite(id: String): Flowable<Boolean> {
        val resultData = localDataSource.isFavorite(id)
        val response = resultData.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { response ->
                    if (response != null) {
                        result2.onNext(true)
                    }
                }, {
                    result2.onNext(false)
                })
        mCompositeDisposable2.add(response)
        return result2.toFlowable(BackpressureStrategy.LATEST)
    }


    private val mCompositeDisposable3 = CompositeDisposable()
    override fun setFavoritePopular(popular: Popular, state: Boolean) {
        val popularEntity = DataMapper.mapDomainToFavEntity(popular)
        localDataSource.setFavoritePopular(popularEntity, state)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e("SET", "BERHASIL")
            }, {
                Log.e("SET", "GAGAL")

            }).let {
                mCompositeDisposable3.add(it)
            }

    }
}

