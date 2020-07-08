package com.shan.foodapp.adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.shan.foodapp.R
import com.shan.foodapp.data_class.MenuList
import com.shan.foodapp.database.CartEntity

class MenuRecyclerAdapter(val context:Context,val itemList:ArrayList<MenuList>):
RecyclerView.Adapter<MenuRecyclerAdapter.MenuViewHolder>(){
    //lateinit var cartSharedPref: SharedPreferences


    class MenuViewHolder(view: View):RecyclerView.ViewHolder(view){
        val menuItem:TextView=view.findViewById(R.id.menuItem)
        val itemCost:TextView=view.findViewById(R.id.itemCost)
        val btnAdd:Button=view.findViewById(R.id.btnAdd)
        val serialNo:TextView=view.findViewById(R.id.serialNo)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_single_row, parent, false)
        return MenuViewHolder(view)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val cartItem=CartEntity(
            holder.menuItem.text.toString(),
            holder.itemCost.text.toString()
        )
        val menu = itemList[position]
        holder.menuItem.text = menu.name
        holder.itemCost.text = menu.cost_for_one
        holder.serialNo.text = "1"

        val checkAdded=dbAsync(context,cartItem,1).execute()
        val added=checkAdded.get()
        if (added){
            holder.btnAdd.text = "Added"
        }
        else{
            holder.btnAdd.text = "Add"
        }
        holder.btnAdd.setOnClickListener {
            if (!checkAdded.get()) {
                val ischeck = dbAsync(context, cartItem, 2).execute()
                val result=ischeck.get()
                if (result){
                    Toast.makeText(context, "added to db", Toast.LENGTH_SHORT).show()
                    holder.btnAdd.text = "Added"
                }else{
                    Toast.makeText(context, "error in adding", Toast.LENGTH_SHORT).show()
                }
            }else{
                val ischeck = dbAsync(context, cartItem, 3).execute()
                val result=ischeck.get()
                if (result){
                    Toast.makeText(context, "deleted frm db", Toast.LENGTH_SHORT).show()
                    holder.btnAdd.text = "Add"
                }else{
                    Toast.makeText(context, "error in deleting", Toast.LENGTH_SHORT).show()
                }
            }
        }


}
    class dbAsync(context: Context, val foodItem:CartEntity, val mode:Int):
            AsyncTask<Void,Void,Boolean>(){
        val cartDb=Room.databaseBuilder (context,com.shan.foodapp.database.CartDb::class.java,"cart-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode){
                1->{
                  val cartItem : CartEntity?=cartDb.cartDao().getCartItemById(foodItem.item_id)
                    cartDb.close()
                    return cartItem!=null
                }
                2->{
                    cartDb.cartDao().insertCartItem(foodItem)
                    cartDb.close()
                    return false
                }
                3->{
                    cartDb.cartDao().deleteCartItem(foodItem)
                    cartDb.close()
                    return false
                }

            }
            return true
        }

    }
}