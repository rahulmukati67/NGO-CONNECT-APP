package com.project.ngoconnectapp

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.project.ngoconnectapp.databinding.ActivityProfileBinding
import java.io.File

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val localFile = File.createTempFile("tempImage", "jpg")
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE

        auth = FirebaseAuth.getInstance()
        val ref =  FirebaseStorage.getInstance().reference.child("images/${auth.currentUser?.uid!!}.jpg")
        ref.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.imgProfile.setImageBitmap(bitmap)
            binding.progressBar.visibility = View.INVISIBLE

        }.addOnFailureListener{
            Toast.makeText(this, "Upload the Profile Picture !", Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.INVISIBLE
        }


        dbRef = FirebaseDatabase.getInstance()
        dbRef.getReference("users").child(auth.currentUser?.uid.toString()).get()
            .addOnCompleteListener {

                val username = it.result.child("username").value
                val email = it.result.child("email").value
                val phoneNumber = it.result.child("phoneNumber").value


                if (email != null) {
                    binding.profileEmail.text = email.toString()
                }
                if (username != null) {
                    binding.profileName.text = username.toString()
                }

                if (phoneNumber != null) {
                    binding.profileNumber.text = phoneNumber.toString()
                }

            }


        binding.ivEdit.setOnClickListener {
            getImage()
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val uri = intent?.data

                binding.progressBar.visibility = View.VISIBLE

                val storageReference = FirebaseStorage.getInstance().getReference("images/" + "${auth.currentUser?.uid!!}.jpg" )
                storageReference.putFile(uri!!).addOnSuccessListener {

                    val stoRef =  FirebaseStorage.getInstance().reference.child("images/${auth.currentUser?.uid!!}.jpg")
                    stoRef.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        binding.imgProfile.setImageBitmap(bitmap)
                        binding.progressBar.visibility = View.INVISIBLE

                    }.addOnFailureListener{
                        Toast.makeText(this, "Failed to Upload File", Toast.LENGTH_SHORT).show()
                    }

                }



            }
        }
    private fun getImage(){

        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI )
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent,"Select Image ")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(Intent(pickIntent)))
        startForResult.launch(chooserIntent)

    }


}