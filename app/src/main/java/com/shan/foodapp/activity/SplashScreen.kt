package com.shan.foodapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.shan.foodapp.R

class SplashScreen : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sharedPreferences=getSharedPreferences("gettingSharedPref", Context.MODE_PRIVATE)
        val login=sharedPreferences.getBoolean("login",false)
        if(login){
            Handler().postDelayed({
                setContentView(R.layout.activity_splash_screen)
                startActivity(Intent(this@SplashScreen,MainActivity::class.java))
                finish()
            },2000
            )
        }else{
            Handler().postDelayed({
                setContentView(R.layout.activity_splash_screen)
                startActivity(Intent(this@SplashScreen,LoginActivity::class.java))
                finish()
            },3000)
        }
    }
}

