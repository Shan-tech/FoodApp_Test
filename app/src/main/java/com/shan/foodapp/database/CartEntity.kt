package com.shan.foodapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Cart")
data class CartEntity (
    @PrimaryKey val item_id:String,
    @ColumnInfo (name="item_cost")val item_cost:String
)
