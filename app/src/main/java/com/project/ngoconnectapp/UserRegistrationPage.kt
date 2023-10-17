package com.project.ngoconnectapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase

class UserRegistrationPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private  lateinit var btngooglesignin:Button
    private lateinit var btncontinue:Button
    private lateinit var userNumber:EditText
    private val app_id = "132639479972-ecvlmdsi49lcfqf9tvsj2iuf5l9734tl.apps.googleusercontent.com"

    private lateinit var googleSignInOptions: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration_page)

        auth = FirebaseAuth.getInstance()
        btngooglesignin = findViewById(R.id.btngooglesignin)
        btncontinue = findViewById(R.id.btncontinue)
        database = FirebaseDatabase.getInstance()
        userNumber = findViewById(R.id.userNumber)
        val number = userNumber.text

        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(app_id)
            .requestEmail()
            .build()
        btngooglesignin.setOnClickListener {
            signInWithGoogle()
        }
        btncontinue.setOnClickListener(View.OnClickListener {
            val intent = Intent(this , OtpActivity::class.java)
            intent.putExtra("phoneNumberr" , number)
//            Toast.makeText(this, "done $number",Toast.LENGTH_LONG).show()
            startActivity(intent)
        })

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
                                val user = User(userId, username, email)
                                usersRef.child(userId).setValue(user)
                            }
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "unable to login try again", Toast.LENGTH_LONG).show()

                        }
                    }
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignInError", "Google sign-in failed with error: ${e.message}")
            Toast.makeText(this, "Google sign-in failed. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}


data class User(
    val userId: String? = "",
    val username: String? = "",
    val email: String? = ""
)
