package com.project.ngoconnectapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.project.ngoconnectapp.databinding.ActivityNgoLoginBinding

class NgoLoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNgoLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNgoLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()


        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailLogin.text.toString()
            val password = binding.etPasswordLogin.text.toString()
            if(email.isEmpty()){
                binding.etEmailLogin.error = "Enter your Email"
            }
            else if(password.isEmpty()){
                binding.etPasswordLogin.error = "Enter Your Password"
            }
            else{

                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    Toast.makeText(this , "Signed In Successfully !", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this , MainActivity::class.java)
                    intent.putExtra("type", "ngo")
                    intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this , it.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }



        }
    }

}