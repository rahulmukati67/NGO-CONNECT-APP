package com.project.ngoconnectapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class GetMoreDetails : AppCompatActivity() {

    private lateinit var btn_continue : Button
    private lateinit var manualUserGmail : EditText
    private lateinit var manualUserName : EditText
    private lateinit var auth : FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_more_details)

        btn_continue = findViewById(R.id.btn_continue)
        manualUserName = findViewById(R.id.manualUserName)
        manualUserGmail = findViewById(R.id.manualUserGmail)

        auth = FirebaseAuth.getInstance()

        firebaseDatabase = FirebaseDatabase.getInstance()
        btn_continue.setOnClickListener{

            val userId = auth.currentUser?.uid
            val username = manualUserName.text.toString()
            val email = manualUserGmail.text.toString()
            val usersRef = firebaseDatabase.getReference("users")


            val user = User(userId, username, email ,auth.currentUser?.phoneNumber)
            usersRef.child(userId!!).setValue(user)


            val intent = Intent(this, MainActivity::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}