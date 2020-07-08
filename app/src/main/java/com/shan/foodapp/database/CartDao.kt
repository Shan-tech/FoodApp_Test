package com.shan.foodapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
@Dao
interface CartDao {
    @Insert
    fun insertCartItem(CartEntity:CartEntity)
    @Delete
    fun deleteCartItem(CartEntity: CartEntity)
    @Query ("SELECT * FROM Cart" )
    fun getAllItemsOnCart():List<CartEntity>
    @Query("SELECT * FROM Cart where item_id=:resId")
    fun getCartItemById(resId:String):CartEntity
}
