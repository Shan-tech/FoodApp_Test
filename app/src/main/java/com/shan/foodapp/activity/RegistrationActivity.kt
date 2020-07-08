package com.shan.foodapp.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shan.foodapp.R
import com.shan.foodapp.fragments.HomeFragment
import com.shan.foodapp.util.ConnectivityManager
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class RegistrationActivity : AppCompatActivity() {
    lateinit var etUsername: EditText
    lateinit var etEmailAddress: EditText
    lateinit var etMobileNumber: EditText
    lateinit var etDeliveryAddress: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnRegister: Button
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        etUsername = findViewById(R.id.etUsername)
        etDeliveryAddress = findViewById(R.id.etDeliveryAddress)
        etEmailAddress = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etNPassword)
        etConfirmPassword = findViewById(R.id.etNConfirmPassword)
        etMobileNumber = findViewById(R.id.etMobileNumber)
        btnRegister = findViewById(R.id.btnRegister)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Registration"

        btnRegister.setOnClickListener {
            val name = etUsername.text.toString()
            val address = etDeliveryAddress.text.toString()
            val mobileNo = etMobileNumber.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            val email=etEmailAddress.text.toString()

            if (ConnectivityManager().checkConnectivity(this@RegistrationActivity)) {
                if (confirmPassword==password) {
                    val queue = Volley.newRequestQueue(this@RegistrationActivity)
                    val url = "http://13.235.250.119/v2/register/fetch_result/"
                    val jsonParams = JSONObject()
                   jsonParams.put("name",name)
                    jsonParams.put("mobile_number",mobileNo)
                    jsonParams.put("password",password)
                    jsonParams.put("address",address)
                    jsonParams.put("email",email)

                    val jsonRequest = object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                            try {
                                val jsonData = it.getJSONObject("data")
                                val success = jsonData.getBoolean("success")
                                if (success) {
                                    val jsonObject = jsonData.getJSONObject("data")
                                     jsonObject.getString("user_id")
                                     jsonObject.getString("name")
                                     jsonObject.getString("email")
                                     jsonObject.getString("mobile_number")
                                     jsonObject.getString("address")
                                    Toast.makeText(this@RegistrationActivity, "Registered successfully", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@RegistrationActivity,MainActivity::class.java))
                                    finish()

                                    /*supportFragmentManager.beginTransaction()
                                        .add(android.R.id.content, HomeFragment ()).commit()*/
                                } else {
                                    Toast.makeText(
                                        this@RegistrationActivity,
                                        "Sorry sm error have been occurred.Try again",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: JSONException) {
                                Toast.makeText(
                                    this@RegistrationActivity,
                                    "sm error on try",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }, Response.ErrorListener {
                            Toast.makeText(
                                this@RegistrationActivity,
                                "error Listener",
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
                    queue.add(jsonRequest)
                }else {
                Toast.makeText(
                    this@RegistrationActivity,
                    "Password doesn't match ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }else{
                val dialog = AlertDialog.Builder(this@RegistrationActivity)
                dialog.setTitle("Error")
                dialog.setMessage("No Internet Connection")
                dialog.setPositiveButton("Open settings") { text, listner ->
                    val settings = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settings)
                    finish()
                }
                dialog.setNegativeButton("exit") { text, listner ->
                    ActivityCompat.finishAffinity(this@RegistrationActivity)
                }
                dialog.create()
                dialog.show()
            }

        }
    }
}
