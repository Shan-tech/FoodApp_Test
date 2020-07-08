package com.shan.foodapp.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.shan.foodapp.R
import com.shan.foodapp.adapter.FavouriteRecyclerAdapter
import com.shan.foodapp.database.FavResDb
import com.shan.foodapp.database.FavRestaurantEntity

class FavouriteFragment : Fragment() {
    lateinit var recyclerFavourite: RecyclerView
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var layoutManager: RecyclerView.LayoutManager
    var favResList= listOf<FavRestaurantEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_favourite, container, false)
        recyclerFavourite=view.findViewById(R.id.favouriteRecycler)
        progressBar=view.findViewById(R.id.progressBar)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressLayout.visibility=View.VISIBLE
        layoutManager= LinearLayoutManager(activity as Context)
        favResList= RetrieveFavouriteRes(activity as Context).execute().get()
        // Inflate the layout for this fragment
        if (activity!=null){
            progressLayout.visibility=View.GONE
            recyclerAdapter= FavouriteRecyclerAdapter(activity as Context,favResList)
            recyclerFavourite.adapter=recyclerAdapter
            recyclerFavourite.layoutManager=layoutManager
        }
        return view
    }
    class RetrieveFavouriteRes(val context: Context):
        AsyncTask<Void,Void,List<FavRestaurantEntity>>(){
        override fun doInBackground(vararg params: Void?): List<FavRestaurantEntity> {
            val db= Room.databaseBuilder(context,FavResDb::class.java,"fav_res-db").build()
            return db.favResDao().getAllRestaurant()
        }
    }
}




