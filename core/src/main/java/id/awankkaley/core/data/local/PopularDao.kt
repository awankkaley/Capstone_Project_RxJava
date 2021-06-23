package id.awankkaley.core.data.local

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

@Dao
interface PopularDao {
    @Query("SELECT * FROM popular")
    fun getAllPopular(): Flowable<List<PopularEntity>>

    @Query("SELECT * FROM popular where title LIKE :search")
    fun searchMovies(search: String): Flowable<List<PopularEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPopular(popular: List<PopularEntity>): Completable

    @Query("SELECT * FROM favorite")
    fun getFavoritePopular(): Flowable<List<PopularEntity>>

    @Query("SELECT * FROM favorite WHERE id = :id")
    fun isFavorite(id: String): Single<PopularEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: FavoriteEntity): Completable

    @Delete
    fun deleteFavorite(favorite: FavoriteEntity): Completable

//    @Update
//    fun updateFavoritePopular(popular: PopularEntity)
}