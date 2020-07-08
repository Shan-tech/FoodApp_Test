package com.shan.foodapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CartEntity::class],version = 1)
abstract class CartDb:RoomDatabase() {
    abstract fun cartDao():CartDao

}