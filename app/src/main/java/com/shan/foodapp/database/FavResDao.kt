package com.shan.foodapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Query

@Dao
interface FavResDao {
    @Insert
    fun insertRestaurant(resEntity: FavRestaurantEntity)

    @Delete
    fun deleteRestaurant(resEntity: FavRestaurantEntity)

    @Query("SELECT * FROM fav_res")
    fun getAllRestaurant():List<FavRestaurantEntity>


    @Query("SELECT * FROM fav_res WHERE res_id=:resId")
    fun getRestaurantById(resId:String):FavRestaurantEntity

}
