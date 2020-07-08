package com.shan.foodapp.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.shan.foodapp.R
import com.shan.foodapp.activity.MenuActivity
import com.shan.foodapp.data_class.ResList
import com.shan.foodapp.database.FavResDb
import com.shan.foodapp.database.FavRestaurantEntity
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context:Context,val itemList: ArrayList<ResList>):
RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {
    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val resImage: ImageView = view.findViewById(R.id.resImage)
        val resName: TextView = view.findViewById(R.id.resName)
        val cost: TextView = view.findViewById(R.id.resCost)
        val rating: TextView = view.findViewById(R.id.resRating)
        val heart: Button = view.findViewById(R.id.heart)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
        val id:String?="100"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_recycler_single_row, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val food = itemList[position]
        holder.resName.text = food.name
        holder.cost.text = food.cost_for_one
        holder.rating.text = food.rating
        val image = food.image_url
        Picasso.get().load(food.image_url).error(R.drawable.ic_launcher_background)
            .into(holder.resImage)
          holder.llContent.setOnClickListener {
            val intent = Intent(context, MenuActivity::class.java)
            intent.putExtra("id", food.resId)
           // var id = food.resId
              intent.putExtra("resName",food.name)
            context.startActivity(intent)
        }
        val favRes = FavRestaurantEntity(
            holder.id?.toInt() as Int,
            holder.resName.text.toString(),
            image,
            holder.cost.text.toString(),
            holder.rating.text.toString()
        )
// whether it is in fav r not
        val checkFav = DbAsync(context, favRes, 1).execute()
        val isFav = checkFav.get()
        if (isFav) {
            // fill color
            val btnImg=ContextCompat.getDrawable(context,R.drawable.ic_baseline_favorite_24)
            holder.heart.background = btnImg
        } else {
            // blank
            val noBtnImg=ContextCompat.getDrawable(context,R.drawable.ic_baseline_favorite_border_24)
            holder.heart.background = noBtnImg
        }
       holder.heart.setOnClickListener {
            if (!DbAsync(context, favRes, 1).execute().get()) {                                                                           //not in fav
                val isClicked = DbAsync(context, favRes, 2).execute()
                val result = isClicked.get()
                if (result) {                                                                                   //fill color
                    Toast.makeText(context,"Res added to fav",Toast.LENGTH_SHORT).show()
                    val btnImg=ContextCompat.getDrawable(context,R.drawable.ic_baseline_favorite_24)
                    holder.heart.background = btnImg
                } else {
                    Toast.makeText(context, "sm error in addingFav", Toast.LENGTH_SHORT).show()
                    }
            } else {                                                                                             //to del frm fav
                val isClicked = DbAsync(context, favRes, 3).execute()
                val result = isClicked.get()
                if (result) {                                                                                 // blank
                    Toast.makeText(context,"Res del form fav",Toast.LENGTH_SHORT).show()
                    val noBtnImg=ContextCompat.getDrawable(context,R.drawable.ic_baseline_favorite_border_24)
                    holder.heart.background = noBtnImg
                } else {
                    Toast.makeText(context, "sm error in del", Toast.LENGTH_SHORT).show()
                      }
            }
        }
    }

    class DbAsync( context: Context, val favResList: FavRestaurantEntity, val mode:Int) :
        AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, FavResDb::class.java, "fav_res-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    // check whether the res is in fav
                    val favRes: FavRestaurantEntity?= db.favResDao().getRestaurantById(favResList.res_id.toString())
                    db.close()
                    return favRes!= null
                }
                2 -> {
                    // add to fav
                    db.favResDao().insertRestaurant(favResList)
                    db.close()
                    return true
                }
                3 -> {
                    //del frm fav
                    db.favResDao().deleteRestaurant(favResList)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
}
