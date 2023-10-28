package com.project.ngoconnectapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {
    private  lateinit var  profileEmail : TextView
    private lateinit var profileName : TextView
    private  lateinit var auth : FirebaseAuth
    private  lateinit var  firebaseDatabase: FirebaseDatabase
    private lateinit var profileNumber: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileEmail = findViewById(R.id.profileEmail)
        profileNumber= findViewById(R.id.profileNumber)
        profileName= findViewById(R.id.profileName)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        var pEmail  = auth.currentUser?.email
        var pNumber = auth.currentUser?.phoneNumber

        var pName = auth.currentUser?.displayName

        if(pEmail!=null) {
            profileEmail.text = pEmail
        }else{
            profileEmail.text = "Not Found"
        }
        if(pName!=null) {
            profileName.text = pName
        }else{
            profileName.text = "Not Found"
        }

        if(pNumber!=null){
        profileNumber.text= pNumber
        }else{
            profileNumber.text = "Not Found"
        }





    }
}