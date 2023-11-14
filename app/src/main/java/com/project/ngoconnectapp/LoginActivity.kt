package com.project.ngoconnectapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.ngoconnectapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLoginUser.setOnClickListener {
            val intent = Intent(this , UserRegistrationPage::class.java)
            intent.putExtra("user", "login")
            startActivity(intent)
            finish()
        }
        binding.btnLoginNgo.setOnClickListener {
            val intent = Intent(this , NgoLoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvRegistration.setOnClickListener {
            val intent = Intent(this , RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}