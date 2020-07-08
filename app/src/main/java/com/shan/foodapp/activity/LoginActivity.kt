package com.shan.foodapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shan.foodapp.R
import com.shan.foodapp.fragments.HomeFragment
import com.shan.foodapp.util.ConnectivityManager
import org.json.JSONObject
import java.util.*

class LoginActivity : AppCompatActivity() {
    lateinit var etMobileNo:EditText
    lateinit var etPassword:EditText
    lateinit var btnLogin:Button
    lateinit var txtForgotPassword:TextView
    lateinit var txtSignup:TextView
    lateinit var sharedPreferences:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etMobileNo=findViewById(R.id.etnumber)
        etPassword=findViewById(R.id.etPassword)
        btnLogin=findViewById(R.id.btnLogin)
        txtForgotPassword=findViewById(R.id.txtForgotPassword)
        txtSignup=findViewById(R.id.txtSignUp)
        sharedPreferences=getSharedPreferences("gettingSharedPref", Context.MODE_PRIVATE)
        /* val login=sharedPreferences.getBoolean("login",false)

       if (login){
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
            finish()
                   }else{
            setContentView(R.layout.activity_login)
        }*/
        etMobileNo=findViewById(R.id.etnumber)
        etPassword=findViewById(R.id.etPassword)
        btnLogin=findViewById(R.id.btnLogin)
        txtForgotPassword=findViewById(R.id.txtForgotPassword)
        txtSignup=findViewById(R.id.txtSignUp)

        btnLogin.setOnClickListener {
            val mobileNo = etMobileNo.text.toString()
            val password=etPassword.text.toString()
            val queue=Volley.newRequestQueue(this@LoginActivity)
            val url="http://13.235.250.119/v2/login/fetch_result"
            val jsonParams=JSONObject()
            if (ConnectivityManager().checkConnectivity(this@LoginActivity)){
                jsonParams.put("mobile_number",mobileNo)
                jsonParams.put("password",password)
                val jsonObjectRequest=object:JsonObjectRequest(Request.Method.POST,url,jsonParams,Response.Listener {
                    try {
                        val jsonData=it.getJSONObject("data")
                        val success=jsonData.getBoolean("success")
                        if (success){
                            val jsonObject = jsonData.getJSONObject("data")
                            sharedPreferences.edit().putString("user_id", jsonObject.getString("user_id")).apply()
                            sharedPreferences.edit().putString("name", jsonObject.getString("name")).apply()
                            sharedPreferences.edit().putString("email",jsonObject.getString("email")).apply()
                            sharedPreferences.edit().putString("number", jsonObject.getString("mobile_number")).apply()
                            sharedPreferences.edit().putString("address",jsonObject.getString("address")).apply()
                            sharedPreferences.edit().putBoolean("login",true).apply()

                            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                            finish()
                              }else{
                            Toast.makeText(this@LoginActivity, "success is false", Toast.LENGTH_SHORT).show()
                        }

                    }catch (e:Exception){
                        Toast.makeText(this@LoginActivity, "error on try", Toast.LENGTH_SHORT).show()
                    }
                },Response.ErrorListener {
                    Toast.makeText(this@LoginActivity, "error Listener", Toast.LENGTH_SHORT).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "bcd19f3799475e"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)
            }else{
                val dialogue=AlertDialog.Builder(this@LoginActivity)
                dialogue.setTitle("Network Error")
                dialogue.setMessage("No internet connection")
                dialogue.setPositiveButton("Open Settings"){ Text,Listner->
                    val settings=Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settings)
                    finish()
                }
                dialogue.setNegativeButton("Exit"){Text,Listner->
                    ActivityCompat.finishAffinity(this@LoginActivity)
                }
                dialogue.create()
                dialogue.show()
            }

        }
        txtForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity,ForgotPasswordActivity::class.java))
        }
        txtSignup.setOnClickListener {
            val reg=Intent(this@LoginActivity,RegistrationActivity::class.java)
            startActivity(reg)
            finish()
        }
    }
}
