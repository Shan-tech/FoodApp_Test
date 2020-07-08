package com.shan.foodapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "fav_res")
data class FavRestaurantEntity (
    @PrimaryKey val res_id:Int,
    @ColumnInfo(name ="res_name" ) val resName:String,
    @ColumnInfo(name = "res_image") val resImage:String,
    @ColumnInfo(name ="res_cost") val resCost_For_one:String,
    @ColumnInfo(name = "res_rating") val resRating:String
)
