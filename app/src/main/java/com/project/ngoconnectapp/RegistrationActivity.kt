package com.project.ngoconnectapp


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class RegistrationActivity : AppCompatActivity() {
    private lateinit var btnNgoReg: CardView
    private lateinit var btnUserReg: CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        btnNgoReg = findViewById(R.id.btnNgoReg)
        btnUserReg = findViewById(R.id.btnUserReg)


        btnNgoReg.setOnClickListener {
            val intent = Intent(this, NgoRegistrationPage::class.java)
            startActivity(intent)
        }
        btnUserReg.setOnClickListener {
            val intent = Intent(this, UserRegistrationPage::class.java)
            startActivity(intent)
        }
    }
}