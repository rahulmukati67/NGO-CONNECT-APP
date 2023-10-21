package com.project.ngoconnectapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

class OtpActivity : AppCompatActivity() {
    private lateinit var btnverify:Button
    private lateinit var verificationCode : EditText
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        auth = FirebaseAuth.getInstance()
        btnverify = findViewById(R.id.btnverify)

        val Number = intent.getStringExtra("phoneNumber")

        Toast.makeText(this, "Code sent to $Number",Toast.LENGTH_LONG).show()

        btnverify.setOnClickListener{
            if (Number != null) {
                Toast.makeText(this, "Code sent to $Number",Toast.LENGTH_LONG).show()
                signInWithNumber(Number)
            }else{
                Toast.makeText(this,"Something went wrong try Again later" ,Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signInWithNumber(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS) // Timeout duration
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(e: FirebaseException) {

                    Toast.makeText(this@OtpActivity, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {

                    val intent = Intent(this@OtpActivity,MainActivity::class.java)
                    startActivity(intent)
                    var storedVerificationId = verificationId
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}