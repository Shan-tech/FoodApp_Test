package com.shan.foodapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shan.foodapp.R
import com.shan.foodapp.util.ConnectivityManager
import org.json.JSONObject

class ResetPasswordActivity : AppCompatActivity() {
    lateinit var otp:EditText
    lateinit var pass:EditText
    lateinit var confirmPass:EditText
    lateinit var submit:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        otp=findViewById(R.id.etOTP)
        pass=findViewById(R.id.etNPassword)
        confirmPass=findViewById(R.id.etNConfirmPassword)
        submit=findViewById(R.id.btnSubmit)
        val number= intent.getStringExtra("number")
        submit.setOnClickListener {
            val OTP=otp.text.toString()
            val password=pass.text.toString()
            val confirmPassword=confirmPass.text.toString()
            if (password==confirmPassword){
                val jsonParams=JSONObject()
                jsonParams.put("mobile_number",number)
                jsonParams.put("password",password)
                jsonParams.put("otp",OTP)
                if (ConnectivityManager().checkConnectivity(this@ResetPasswordActivity)) {
                    val queue = Volley.newRequestQueue(this@ResetPasswordActivity)
                    val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                    val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST,url,jsonParams,
                        Response.Listener {
                        val data=it.getJSONObject("data")
                            val success=data.getBoolean("success")
                            if (success){
                                Toast.makeText(this@ResetPasswordActivity, "Password reset successfully", Toast.LENGTH_SHORT).show()
                              startActivity(Intent(this@ResetPasswordActivity,MainActivity::class.java))
                                finish()
                            }else{
                                Toast.makeText(this@ResetPasswordActivity, "success false", Toast.LENGTH_SHORT).show()
                            }
                    },
                        Response.ErrorListener {
                        Toast.makeText(this@ResetPasswordActivity, "error listener", Toast.LENGTH_SHORT).show()
                    }){
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "bcd19f3799475e"
                            return headers
                        }
                    }
                    queue.add(jsonObjectRequest)
                }else{
                    val dialogue= AlertDialog.Builder(this@ResetPasswordActivity)
                    dialogue.setTitle("Network Error")
                    dialogue.setMessage("No internet connection")
                    dialogue.setPositiveButton("Open Settings"){ Text,Listner->
                        val settings= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settings)
                        finish()
                    }
                    dialogue.setNegativeButton("Exit"){Text,Listner->
                        ActivityCompat.finishAffinity(this@ResetPasswordActivity)
                    }
                    dialogue.create()
                    dialogue.show()
                }
            }else{
                Toast.makeText(this@ResetPasswordActivity, "Password doesn't match.Re-enter pls", Toast.LENGTH_SHORT).show()
            }
        }
    }
}