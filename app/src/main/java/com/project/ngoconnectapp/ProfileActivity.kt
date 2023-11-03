package com.project.ngoconnectapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.project.ngoconnectapp.databinding.ActivityProfileBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()

        dbRef = FirebaseDatabase.getInstance()
        dbRef.getReference("users").child(auth.currentUser?.uid.toString()).get()
            .addOnCompleteListener {

                val username = it.result.child("username").value
                val email = it.result.child("email").value
                val phoneNumber = it.result.child("phoneNumber").value


                if (email != null) {
                    binding.profileEmail.text = email.toString()
                } else {
                    binding.profileEmail.text = "Not Found"
                }
                if (username != null) {
                    binding.profileName.text = username.toString()
                } else {
                    binding.profileName.text = "Not Found"
                }

                if (phoneNumber != null) {
                    binding.profileNumber.text = phoneNumber.toString()
                } else {
                    binding.profileNumber.text = "Not Found"
                }

            }

        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    val uri = intent?.data

                    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA)
                    val filename = formatter.format(Date())
                    val storageReference = FirebaseStorage.getInstance().getReference("images/" + "${auth.currentUser?.uid!!}/" + filename )
                    storageReference.putFile(uri!!)

                    binding.imgProfile.setImageURI(uri)
                }
            }

        binding.ivEdit.setOnClickListener {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"

            val pickIntent = Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI )
            pickIntent.type = "image/*"

            val chooserIntent = Intent.createChooser(getIntent,"Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(Intent(pickIntent)))
            startForResult.launch(chooserIntent)
        }
    }


}