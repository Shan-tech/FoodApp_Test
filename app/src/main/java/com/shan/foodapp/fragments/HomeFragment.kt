package com.shan.foodapp.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.shan.foodapp.R
import com.shan.foodapp.activity.CartActivity
import com.shan.foodapp.activity.MainActivity
import com.shan.foodapp.activity.MenuActivity
import com.shan.foodapp.adapter.HomeRecyclerAdapter
import com.shan.foodapp.data_class.ResList
import com.shan.foodapp.database.FavResDb
import com.shan.foodapp.database.FavRestaurantEntity
import com.shan.foodapp.util.ConnectivityManager

class HomeFragment : Fragment() {


    lateinit var homeFrag: RelativeLayout
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var homeRecycler: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    lateinit var navigationView: NavigationView

    lateinit var toolbar: Toolbar
    var resList = arrayListOf<ResList>()
    var previousMenuItem:MenuItem?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        setHasOptionsMenu(true)
        homeFrag = view.findViewById(R.id.homeFrag)
        homeRecycler = view.findViewById(R.id.homeRecyclerView)
        layoutManager = LinearLayoutManager(activity)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
        if (ConnectivityManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    try {
                        progressLayout.visibility = View.GONE
                        val dataJson = it.getJSONObject("data")
                        val success = dataJson.getBoolean("success")
                        if (success) {
                            val data = dataJson.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val resListJsonObject = data.getJSONObject(i)
                                val resListObject = ResList(
                                    resListJsonObject.getString("id"),
                                    resListJsonObject.getString("name"),
                                    resListJsonObject.getString("rating"),
                                    resListJsonObject.getString("cost_for_one"),
                                    resListJsonObject.getString("image_url")
                                )
                                resList.add(resListObject)
                                recyclerAdapter = HomeRecyclerAdapter(activity as Context, resList)
                                homeRecycler.adapter = recyclerAdapter
                                homeRecycler.layoutManager = layoutManager
                            }

                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some json error may occurred",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            activity as Context,
                            "Some error may occurred",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }, Response.ErrorListener {
                    if (activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley error may occurred!",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "bcd19f3799475e"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Poor Internet Connection")
            dialog.setPositiveButton("Open settings") { text, listner ->
                val settings = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settings)
                activity?.finish()
            }
            dialog.setNegativeButton("exit") { text, listner ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
        return view
    }


}
