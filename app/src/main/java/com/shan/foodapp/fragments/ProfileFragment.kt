package com.shan.foodapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.TestLooperManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.shan.foodapp.R

class ProfileFragment : Fragment() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var name:TextView
    lateinit var number:TextView
    lateinit var email:TextView
    lateinit var address:TextView
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)
        sharedPreferences=(activity as Context).getSharedPreferences("gettingSharedPref", Context.MODE_PRIVATE)
        name = view.findViewById(R.id.name)
        number = view.findViewById(R.id.number)
        email = view.findViewById(R.id.email)
        address = view.findViewById(R.id.address)
        name.text = sharedPreferences.getString("name", null)
        number.text = "+91-${sharedPreferences.getString("number", null)}"
        email.text = sharedPreferences.getString("email", null)
        address.text = sharedPreferences.getString("address", null)
        return view
    }
}

