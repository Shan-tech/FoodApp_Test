package com.shan.foodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shan.foodapp.R
import com.shan.foodapp.database.FavRestaurantEntity
import com.squareup.picasso.Picasso
class FavouriteRecyclerAdapter (val context: Context,val favResList:List<FavRestaurantEntity>):
RecyclerView.Adapter<FavouriteRecyclerAdapter.FavViewHolder>() {


    class FavViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val resImage: ImageView = view.findViewById(R.id.resImage)
        val resName: TextView = view.findViewById(R.id.resName)
        val cost_for_one: TextView = view.findViewById(R.id.resCost)
        val rating: TextView = view.findViewById(R.id.resRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.favourite_restaurant_single_row,parent,false)
        return FavViewHolder(view)
    }

    override fun getItemCount(): Int {
      return favResList.size
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val food = favResList[position]
        holder.resName.text = food.resName
        holder.cost_for_one.text = food.resCost_For_one
        holder.rating.text = food.resRating
        Picasso.get().load(food.resImage).error(R.drawable.ic_launcher_background)
            .into(holder.resImage)
    }
}