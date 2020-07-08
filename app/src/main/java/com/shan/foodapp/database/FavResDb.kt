package com.shan.foodapp.database


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavRestaurantEntity::class],version = 1)
abstract class FavResDb:RoomDatabase() {

    abstract fun favResDao():FavResDao

}


