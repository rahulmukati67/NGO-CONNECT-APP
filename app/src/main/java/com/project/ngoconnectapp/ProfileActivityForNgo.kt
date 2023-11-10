package com.project.ngoconnectapp

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.project.ngoconnectapp.databinding.ActivityProfileForNgoBinding
import java.io.File

class ProfileActivityForNgo : AppCompatActivity() {

    private lateinit var binding: ActivityProfileForNgoBinding
    private lateinit var dbRef : FirebaseDatabase
    private lateinit var storageRef : FirebaseStorage
    private lateinit var auth : FirebaseAuth
    private val localFile =  File.createTempFile("tempFile","jpg")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileForNgoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance()
        storageRef = FirebaseStorage.getInstance()

        getImageFromFirebase()

        val regNumber = intent.getStringExtra("regId")
        binding.tvRegNumNgo.text = regNumber

        getDataFromFirebase()


        binding.ivEditPhotoNgo.setOnClickListener {

            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"

            val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            pickIntent.type ="image/*"

            val chooserIntent = Intent.createChooser(getIntent, "Select Image ")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
            startForResult.launch(chooserIntent)

        }

    }

     private val startForResult =
         registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result : ActivityResult ->
             if(result.resultCode == Activity.RESULT_OK){
                 val intent = result.data
                 val uri = intent?.data

                 val ref = storageRef.getReference("ngoImages/${auth.currentUser?.uid}.jpg")

                 ref.putFile(uri!!).addOnSuccessListener {

                     getImageFromFirebase()
                 }.addOnFailureListener{
                     Toast.makeText(this, "Failed to upload the image !", Toast.LENGTH_SHORT).show()
                 }
             }
         }

    private fun getImageFromFirebase(){
        val ref = storageRef.reference.child("ngoImages/${auth.currentUser?.uid}.jpg")
        ref.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.imgNgoProfile.setImageBitmap(bitmap)

            binding.progressBar3.visibility = View.INVISIBLE

        }.addOnFailureListener {
            Toast.makeText(this , "Upload the Profile Picture !", Toast.LENGTH_SHORT).show()
            binding.progressBar3.visibility = View.INVISIBLE
        }
     }

    private fun getDataFromFirebase(){
        val regNumber = intent.getStringExtra("regId")
        dbRef.getReference("ngoDetails").child(regNumber!!).get().addOnCompleteListener {

            var ngoName = it.result.child("name").value.toString()
            var ngoType = it.result.child("ngoType").value.toString()
            var ngoEmail = it.result.child("emailId").value.toString()
            var ngoNumber = it.result.child("phoneNo").value.toString()
            var ngoWebsite = it.result.child("ngoWeb").value.toString()

            binding.tvNgoName.text = ngoName
            binding.tvTypeNgo.text = ngoType
            binding.tvEmailNgo.text = ngoEmail
            binding.tvNumberNgo.text = ngoNumber
            binding.tvWebsiteNgo.text = ngoWebsite
        }
    }
}