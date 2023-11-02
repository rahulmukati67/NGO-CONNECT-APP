package com.project.ngoconnectapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {
    private  lateinit var  profileEmail : TextView
    private lateinit var profileName : TextView
    private  lateinit var auth : FirebaseAuth
    private lateinit var dbRef : FirebaseDatabase
    private lateinit var profileNumber: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileEmail = findViewById(R.id.profileEmail)
        profileNumber= findViewById(R.id.profileNumber)
        profileName= findViewById(R.id.profileName)

        auth = FirebaseAuth.getInstance()

        dbRef = FirebaseDatabase.getInstance()
        dbRef.getReference("users").child(auth.currentUser?.uid.toString()).get().addOnCompleteListener {

            val username = it.result.child("username").value
            val email = it.result.child("email").value
            val phoneNumber = it.result.child("phoneNumber").value


        if(email!=null) {
            profileEmail.text = email.toString()
        }else{
            profileEmail.text = "Not Found"
        }
        if(username!=null) {
            profileName.text = username.toString()
        }else{
            profileName.text = "Not Found"
        }

        if(phoneNumber!=null){
        profileNumber.text= phoneNumber.toString()
        }else{
            profileNumber.text = "Not Found"
        }

        }


    }
}