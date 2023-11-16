package com.project.ngoconnectapp


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegistrationActivity : AppCompatActivity() {
    private lateinit var btnNgoReg: Button
    private lateinit var btnUserReg: Button
    private  lateinit var btnlogin1 : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        btnNgoReg = findViewById(R.id.btnNgoReg)
        btnUserReg = findViewById(R.id.btnUserReg)
        btnlogin1 = findViewById(R.id.btnlogin1)


        btnNgoReg.setOnClickListener {
            val intent = Intent(this, NgoRegistrationPage::class.java)
            startActivity(intent)
        }
        btnUserReg.setOnClickListener {
            val intent = Intent(this, UserRegistrationPage::class.java)
            startActivity(intent)
        }
        btnlogin1.setOnClickListener{
            val intent = Intent(this , LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}