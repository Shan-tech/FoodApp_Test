package com.shan.foodapp.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shan.foodapp.R
import com.shan.foodapp.adapter.MenuRecyclerAdapter
import com.shan.foodapp.data_class.MenuList
import java.lang.Exception

class MenuActivity : AppCompatActivity() {


    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var recyclerAdapter:MenuRecyclerAdapter
    lateinit var menuRecycler: RecyclerView
    lateinit var toolbar: Toolbar
    var menuList = arrayListOf<MenuList>()
    var resId:String?="100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_fragment)
        menuRecycler = findViewById(R.id.menuRecycler)
        layoutManager = LinearLayoutManager(this@MenuActivity)
        toolbar = findViewById(R.id.toolbar)
        progressBar = findViewById(R.id.ProgressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.ProgressLayout)
        progressLayout.visibility = View.VISIBLE

        val resName=intent.getStringExtra("resName")
        if (intent != null) {
            resId = intent.getStringExtra("id")
            setSupportActionBar(toolbar)
            supportActionBar?.title= resName
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

        } else {
            finish()
            Toast.makeText(this@MenuActivity, "Sm error has been occurred", Toast.LENGTH_SHORT)
                .show()
        }
        if (resId == "100") {
            finish()
            Toast.makeText(this@MenuActivity, "Sm error has been occurred", Toast.LENGTH_SHORT)
                .show()
        }

        val queue = Volley.newRequestQueue(this@MenuActivity)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$resId"
        if (com.shan.foodapp.util.ConnectivityManager().checkConnectivity(this@MenuActivity)) {
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    try {
                        progressLayout.visibility = View.GONE
                        progressBar.visibility = View.GONE

                        val dataObj = it.getJSONObject("data")
                        val success = dataObj.getBoolean("success")
                        if (success) {
                            val data = dataObj.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val resListJsonObject = data.getJSONObject(i)
                                val resListObject = MenuList(
                                    resListJsonObject.getString("restaurant_id"),
                                    resListJsonObject.getString("name"),
                                    resListJsonObject.getString("cost_for_one")
                                )
                                menuList.add(resListObject)
                                recyclerAdapter = MenuRecyclerAdapter(this@MenuActivity, menuList)
                                menuRecycler.adapter = recyclerAdapter
                                menuRecycler.layoutManager = layoutManager
                            }
                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@MenuActivity,
                            "Some json error may occurred catch",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(
                        this@MenuActivity,
                        "Some json error may occurred EL", Toast.LENGTH_SHORT
                    ).show()
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
            val dialog = AlertDialog.Builder(this@MenuActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Poor Internet Connection")
            dialog.setPositiveButton("Open settings") { text, listner ->
                val settings = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settings)
                finish()
            }
            dialog.setNegativeButton("exit") { text, listner ->
                ActivityCompat.finishAffinity(this@MenuActivity)
            }
            dialog.create()
            dialog.show()
        }
    }

}




