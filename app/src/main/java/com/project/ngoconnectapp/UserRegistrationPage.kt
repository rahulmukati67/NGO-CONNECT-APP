package com.project.ngoconnectapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.project.ngoconnectapp.databinding.ActivityUserRegistrationPageBinding
class UserRegistrationPage : AppCompatActivity() {

    private lateinit var binding: ActivityUserRegistrationPageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val appId = "132639479972-ecvlmdsi49lcfqf9tvsj2iuf5l9734tl.apps.googleusercontent.com"

    private lateinit var googleSignInOptions: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserRegistrationPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val value = intent.getStringExtra("user")
        if(value == "login"){
            binding.btngooglesignin.text = getString(R.string.login_google)
            binding.tvRegisterUser.text = getString(R.string.login_user)
        }
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(appId)
            .requestEmail()
            .build()

        binding.btngooglesignin.setOnClickListener {
            signInWithGoogle()
        }

        binding.btnContinue.setOnClickListener {
            var number = binding.userNumber.text.toString().trim()
            if (number.isNotEmpty() && number.length == 10) {
                number = "+91$number"

                val intent = Intent(this, OtpActivity::class.java)
                intent.putExtra("phoneNumber", number)
                startActivity(intent)
            } else {
                binding.userNumber.error =" PLease enter a valid number"
            }

        }

    }


    private fun signInWithGoogle() {
        val signInIntent = GoogleSignIn.getClient(this, googleSignInOptions).signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account?.idToken

            if (idToken != null) {
                val credential = GoogleAuthProvider.getCredential(idToken, null)

                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                val username = auth.currentUser?.displayName ?: ""
                                val email = auth.currentUser?.email

                                val usersRef = database.getReference("users")
                                val user = User(userId, username, email ,"")
                                usersRef.child(userId).setValue(user)

                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                intent.putExtra("type", "user")
                                startActivity(intent)
                            }

                        } else {
                            Toast.makeText(this, "Unable to login, try again", Toast.LENGTH_LONG)
                                .show()

                        }
                    }
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignInError", "Google sign-in failed with error: ${e.message}")
            Toast.makeText(this, "Google sign-in failed. Please try again.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }




}

