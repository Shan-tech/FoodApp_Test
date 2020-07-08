package com.shan.foodapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.View.inflate
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.core.graphics.drawable.DrawableCompat.inflate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import com.shan.foodapp.*
import com.shan.foodapp.fragments.FavouriteFragment
import com.shan.foodapp.fragments.HomeFragment
import com.shan.foodapp.fragments.ProfileFragment
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var appBarLayout: AppBarLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var sharedPreferences: SharedPreferences
    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        appBarLayout = findViewById(R.id.appBarLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigationView)
        sharedPreferences=getSharedPreferences("gettingSharedPref", Context.MODE_PRIVATE)

        setUpToolbar()
        openDashboardFragment()
        val actionBarToggle = ActionBarDrawerToggle(
            this@MainActivity, drawerLayout, R.string.open_drawer, R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
                it.isChecked = true
            }
            it.isCheckable = true
            previousMenuItem = it
            when (it.itemId) {
                R.id.home -> {
                    openDashboardFragment()
                    drawerLayout.closeDrawers()
                }
                R.id.favourite -> {
                    supportActionBar?.title = "Favourite"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FavouriteFragment())
                        // .addToBackStack("favourites") to store it in container to get it back
                        .commit()
                    drawerLayout.closeDrawers()
                }
                R.id.myProfile -> {
                    supportActionBar?.title = "My Profile"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, ProfileFragment())
                        .commit()
                    drawerLayout.closeDrawers()
                }
                R.id.faq -> {
                    supportActionBar?.title = "FAQ's"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FaqFragment())
                        .commit()
                    drawerLayout.closeDrawers()
                }
                R.id.logout -> {
                    val dialog= AlertDialog.Builder(this)
                    dialog.setTitle("Logout")
                    dialog.setMessage("Are you sure ?")
                    dialog.setPositiveButton("Yes"){_,_->
                        sharedPreferences.edit().clear().apply()
                        startActivity(Intent(this,LoginActivity::class.java))
                        finish()
                    }
                    dialog.setNegativeButton("No"){_,_->
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }
                    dialog.create()
                    dialog.show()

                    drawerLayout.closeDrawers()

                }
            }
            return@setNavigationItemSelectedListener true//lamda
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        when (item.itemId) {
            R.id.cart -> {
                startActivity(Intent(this@MainActivity, CartActivity::class.java))
            }
            R.id.OrderHistory -> {
                Toast.makeText(this, "orderHistory ", Toast.LENGTH_SHORT).show()
            }
            R.id.sort -> {
                Toast.makeText(this, "sort ", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Action Bar"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun openDashboardFragment() {
        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        supportActionBar?.title = "Dashboard"
        transaction.replace(R.id.frameLayout, HomeFragment())
        transaction.commit()
        navigationView.setCheckedItem(R.id.home)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)
        when (frag) {
            !is HomeFragment -> openDashboardFragment()
            else -> super.onBackPressed()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       val  inflate=MenuInflater(this)
        inflate.inflate(R.menu.menu_home, menu)
        return true
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort -> {
                startActivity(Intent(this@MainActivity, CartActivity::class.java))
            }
            R.id.OrderHistory -> {
                Toast.makeText(this, "orderHistory ", Toast.LENGTH_SHORT).show()
            }
            R.id.cart -> {
                Toast.makeText(this@HomeFragment, "cart ", Toast.LENGTH_SHORT).show()
            }
            else->{
                Toast.makeText(this@HomeFragment, "cart ", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }*/
}


