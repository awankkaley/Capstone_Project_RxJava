package id.awankkaley.core.data.local

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val popularDao: PopularDao) {

    fun getAllPopular(): Flowable<List<PopularEntity>> = popularDao.getAllPopular()

    //    fun searchMoviews(query: String): Flow<List<PopularEntity>> = popularDao.searchMovies(query)
    fun getFavoritePopular(): Flowable<List<PopularEntity>> = popularDao.getFavoritePopular()

    fun isFavorite(id: String): Single<PopularEntity?> {
        return popularDao.isFavorite(id)
    }

    fun insertPopular(tourismList: List<PopularEntity>) =
        popularDao.insertPopular(tourismList)


    @SuppressLint("CheckResult")
    fun setFavoritePopular(favorite: FavoriteEntity, newState: Boolean): Completable {
        return if (newState) {
            popularDao.insertFavorite(favorite)
        } else {
            popularDao.deleteFavorite(favorite)
        }
    }

}