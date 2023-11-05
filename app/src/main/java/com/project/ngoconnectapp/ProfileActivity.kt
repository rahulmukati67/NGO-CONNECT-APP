package com.project.ngoconnectapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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
    private lateinit var username: String
    private lateinit var email: String
    private lateinit var phoneNumber: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE

        auth = FirebaseAuth.getInstance()

        getImageFromFirebase()
        getDataFromFirebase()

        binding.ivEdit.setOnClickListener {
            getImage()
        }
        binding.ivEditName.setOnClickListener {
            createDialog("Updating UserName..", username, "username")

        }
        binding.ivEditNumber.setOnClickListener {
            createDialog("Updating Contact..." , phoneNumber, "phoneNumber")
        }
        binding.ivEditEmail.setOnClickListener {
            createDialog("Updating Email.." , email , "email")
        }
        binding.ivBackBtn.setOnClickListener {
            finish()
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val uri = intent?.data

                binding.progressBar.visibility = View.VISIBLE

                val storageReference = FirebaseStorage.getInstance()
                    .getReference("images/" + "${auth.currentUser?.uid!!}.jpg")
                storageReference.putFile(uri!!).addOnSuccessListener {

                    getImageFromFirebase()

                }
            }
        }

    private fun getImage() {

        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image ")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(Intent(pickIntent)))
        startForResult.launch(chooserIntent)

    }

    private fun createDialog(title: String , infoDetail : String , type: String)  {
        val dialog = AlertDialog.Builder(this, androidx.appcompat.R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        val dialogView = layoutInflater.inflate(R.layout.update_dialog, null)

        dialog.setView(dialogView)
        dialog.setTitle(title)

        val alertDialog = dialog.create()
        alertDialog.show()

        val info = dialogView.findViewById<EditText>(R.id.etUserDetail)
        val btnUpdate = dialogView.findViewById<Button>(R.id.btnUpdate)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        info.setText(infoDetail)

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        btnUpdate.setOnClickListener {
        var user : User? = User("", "", "","")
            when (type) {
                "username" -> {
                    user = User(auth.currentUser?.uid!!,info.text.toString(),email,phoneNumber )
                }
                "email" -> {
                    user = User(auth.currentUser?.uid!!,username,info.text.toString(),phoneNumber )
                }
                "phoneNumber" -> {
                    user = User(auth.currentUser?.uid!!,username,email,info.text.toString())
                }
            }

            dbRef.getReference("users").child(auth.currentUser?.uid!!.toString()).setValue(user!!).addOnCompleteListener {
                Toast.makeText(this, "Successfully data updated !", Toast.LENGTH_SHORT).show()
            }
            alertDialog.dismiss()
            getDataFromFirebase()
        }

    }

    private fun getDataFromFirebase() {
        dbRef = FirebaseDatabase.getInstance()
        dbRef.getReference("users").child(auth.currentUser?.uid.toString()).get()
            .addOnCompleteListener {

                username = it.result.child("username").value.toString()
                email = it.result.child("email").value.toString()
                phoneNumber = it.result.child("phoneNumber").value.toString()

                    binding.profileEmail.text = email
                    binding.profileName.text = username
                    binding.profileNumber.text = phoneNumber

            }

        if (auth.currentUser?.email == "") {
            binding.ivEditEmail.visibility = View.VISIBLE
        }
        if (auth.currentUser?.phoneNumber == "") {
            binding.ivEditNumber.visibility = View.VISIBLE
        }
    }

    private fun getImageFromFirebase(){
        val ref =
            FirebaseStorage.getInstance().reference.child("images/${auth.currentUser?.uid!!}.jpg")
        ref.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.imgProfile.setImageBitmap(bitmap)
            binding.progressBar.visibility = View.INVISIBLE

        }.addOnFailureListener {
            Toast.makeText(this, "Upload the Profile Picture !", Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.INVISIBLE
        }
    }


}