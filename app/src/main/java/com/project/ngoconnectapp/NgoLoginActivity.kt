package com.project.ngoconnectapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.project.ngoconnectapp.databinding.ActivityNgoLoginBinding

class NgoLoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNgoLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNgoLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()
        val regId = FirebaseDatabase.getInstance().getReference("ngoDetails")

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailLogin.text.toString()
            val password = binding.etPasswordLogin.text.toString()


            auth.signInWithEmailAndPassword(email , password).addOnSuccessListener {
                val intent = Intent(this , MainActivity::class.java)
                intent.putExtra("type", "ngo")
//                intent.putExtra("regId", regId)
                intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

}