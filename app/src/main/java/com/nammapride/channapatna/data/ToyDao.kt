package com.nammapride.channapatna.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ToyDao {
    @Query("SELECT * FROM toys ORDER BY id DESC")
    fun observeAll(): Flow<List<ToyEntity>>

    @Query("SELECT COUNT(*) FROM toys")
    suspend fun count(): Int

    @Query("SELECT name FROM toys")
    suspend fun names(): List<String>

    @Query(
        """
        UPDATE toys
        SET category = :category,
            price = :price,
            rating = :rating,
            artisan = :artisan,
            image = :image
        WHERE name = :name
        """
    )
    suspend fun updateByName(
        name: String,
        category: String,
        price: String,
        rating: String,
        artisan: String,
        image: String
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(toy: ToyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(toys: List<ToyEntity>)
}
