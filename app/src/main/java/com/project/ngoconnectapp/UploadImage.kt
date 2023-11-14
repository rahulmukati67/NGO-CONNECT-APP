package com.project.ngoconnectapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.project.ngoconnectapp.databinding.ActivityUploadImageBinding

class UploadImage : AppCompatActivity() {
    private lateinit var binding : ActivityUploadImageBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var storageRef : StorageReference
    private lateinit var dbRef : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance().getReference("ngoImages/${auth.currentUser?.uid!!}.jpg")
        dbRef = FirebaseDatabase.getInstance().getReference("ngoDetails").child(auth.currentUser?.uid!!)
            .child("ngoImage")

        binding.btnSelectImage.setOnClickListener {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type ="image/*"
            val pickIntent = Intent(Intent.ACTION_PICK)
            pickIntent.type = "image/*"
            val chooserIntent = Intent.createChooser(getIntent, "Select Image..")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
            startForResult.launch(chooserIntent)
        }

    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result : ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val uri = result.data?.data

            storageRef.putFile(uri!!).addOnSuccessListener {

                binding.imgNgoUpload.setImageURI(uri)
                Toast.makeText(this , "Image uploaded Successfully !", Toast.LENGTH_SHORT).show()

                it.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri->

                    dbRef.setValue(uri.toString())
                    val intent = Intent(this , MainActivity::class.java)
                    intent.putExtra("type", "ngo")
                    intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }.addOnFailureListener {
                Toast.makeText(this , it.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}