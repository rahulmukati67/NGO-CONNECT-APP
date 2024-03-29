package com.project.ngoconnectapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.project.ngoconnectapp.databinding.ActivityOtpBinding
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storedVerificationId: String
    private lateinit var database: FirebaseDatabase
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var number: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar2.visibility = View.VISIBLE
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        number = intent.getStringExtra("phoneNumber").toString()

        signInWithNumber()
        binding.btnverify.setOnClickListener {
            val otp = binding.verificationCode.text.toString().trim()
            val credential = PhoneAuthProvider.getCredential(storedVerificationId, otp)
            signInWithPhoneAuthenticationCredential(credential)
        }

        binding.btnResend.setOnClickListener {
            Toast.makeText(this, "Resending...",Toast.LENGTH_SHORT).show()
            reSendOtp()
        }

    }

    private fun signInWithNumber() {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(30L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }


    private fun signInWithPhoneAuthenticationCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val userRef = database.getReference("users")
                    userRef.child(userId!!).setValue(User(userId, "", "", number))
                    Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, GetMoreDetails::class.java)
                    intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "${task.exception}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun reSendOtp() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(30L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(resendToken) // ForceResendingToken from callbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthenticationCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {

            Toast.makeText(
                this@OtpActivity, "Verification failed: ${e.message}", Toast.LENGTH_SHORT
            ).show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            storedVerificationId = verificationId
            resendToken = token
            binding.progressBar2.visibility = View.INVISIBLE
            Toast.makeText(this@OtpActivity, "Code sent to $number", Toast.LENGTH_SHORT).show()
            binding.btnverify.isEnabled = true
        }
    }

}