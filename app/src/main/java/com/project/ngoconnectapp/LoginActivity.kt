package com.project.ngoconnectapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btnLoginNgo = findViewById<Button>(R.id.btnLoginNgo)
        val btnLoginUser = findViewById<Button>(R.id.btnLoginUser)
        btnLoginUser.setOnClickListener {
            val intent = Intent(this , UserRegistrationPage::class.java)
            intent.putExtra("user", "login")
            startActivity(intent)
        }
        btnLoginNgo.setOnClickListener {
            val intent = Intent(this , NgoLoginActivity::class.java)
            startActivity(intent)
        }

    }
}