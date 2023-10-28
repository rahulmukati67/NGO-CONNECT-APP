package com.project.ngoconnectapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.childEvents

class ProfileActivity : AppCompatActivity() {
    private  lateinit var  profileEmail : TextView
    private lateinit var profileName : TextView
    private  lateinit var auth : FirebaseAuth
    private  lateinit var  firebaseDatabase: FirebaseDatabase
    private lateinit var profileNumber: TextView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseSnapshot: DataSnapshot
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileEmail = findViewById(R.id.profileEmail)
        profileNumber= findViewById(R.id.profileNumber)
        profileName= findViewById(R.id.profileName)

        auth = FirebaseAuth.getInstance()
        val userRef =
            auth.currentUser?.let { FirebaseDatabase.getInstance().reference.child("users").child(it.uid) }



        var pEmail  =  auth.currentUser?.email
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