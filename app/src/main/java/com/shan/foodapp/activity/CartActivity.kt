package com.shan.foodapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.shan.foodapp.R



class CartActivity : AppCompatActivity() {
    lateinit var toolbar:Toolbar
    lateinit var cardCart:CardView
    lateinit var res:TextView
    lateinit var recycler: RecyclerView.Recycler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        toolbar=findViewById(R.id.toolbar)
        res=findViewById(R.id.OrderFrm)
        cardCart=findViewById(R.id.cardCart)
      //  recycler=findViewById(R.id.recyclerCart)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Cart"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val resName=intent.getStringExtra("resName")
        res.text = "Ordered From:$resName"
    }
}