package com.shan.foodapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.json.JSONObject
import java.lang.Exception

class ForgotPasswordActivity : AppCompatActivity() {
     lateinit var mobileNumber:EditText
     lateinit var email:EditText
     lateinit var next:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        mobileNumber=findViewById(R.id.etnumber)
        email=findViewById(R.id.etEmail)
        next=findViewById(R.id.btnNext)

        next.setOnClickListener {
            val number=mobileNumber.text.toString()
            val email=email.text.toString()

            val queue=Volley.newRequestQueue(this@ForgotPasswordActivity)
            val url="http://13.235.250.119/v2/forgot_password/fetch_result"
            val jsonParams=JSONObject()
            jsonParams.put("mobile_number",number)
            jsonParams.put("email",email)
            if (ConnectivityManager().checkConnectivity(this@ForgotPasswordActivity)) {
                val jsonObjectRequest = object :
                    JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                        try {
                            val data=it.getJSONObject("data")
                            val success=data.getBoolean("success")
                            if(success){
                                Toast.makeText(this@ForgotPasswordActivity, "$success OTP sent ", Toast.LENGTH_SHORT).show()
                                val i=Intent(this@ForgotPasswordActivity,ResetPasswordActivity::class.java)
                                i.putExtra("number",number)
                                startActivity(i)
                                finish()
                            }
                            else{
                                Toast.makeText(this@ForgotPasswordActivity, "$success sm thng wrng ", Toast.LENGTH_SHORT).show()
                            }
                        }catch (e:Exception){
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "try error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "error listener",
                            Toast.LENGTH_SHORT
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
            }else{
                val dialogue=AlertDialog.Builder(this@ForgotPasswordActivity)
                dialogue.setTitle("Network Error")
                dialogue.setMessage("No internet connection")
                dialogue.setPositiveButton("Open Settings"){ Text,Listner->
                    val settings= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settings)
                    finish()
                }
                dialogue.setNegativeButton("Exit"){Text,Listner->
                    ActivityCompat.finishAffinity(this@ForgotPasswordActivity)
                }
                dialogue.create()
                dialogue.show()
            }

        }
    }
}
