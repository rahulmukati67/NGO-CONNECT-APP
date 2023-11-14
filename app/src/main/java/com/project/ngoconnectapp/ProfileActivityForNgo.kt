package com.project.ngoconnectapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.project.ngoconnectapp.databinding.ActivityProfileForNgoBinding
import java.io.File

class ProfileActivityForNgo : AppCompatActivity() {

    private lateinit var binding: ActivityProfileForNgoBinding
    private lateinit var dbRef : DatabaseReference
    private lateinit var storageRef : FirebaseStorage
    private lateinit var auth : FirebaseAuth
    private val localFile =  File.createTempFile("tempFile","jpg")
    private lateinit var ngoType :String
    private lateinit var ngoNumber :String
    private lateinit var ngoWebsite :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileForNgoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar3.visibility = View.VISIBLE

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("ngoDetails").child(auth.currentUser?.uid!!)
        storageRef = FirebaseStorage.getInstance()

        getImageFromFirebase()
        getDataFromFirebase()

        binding.ivEditPhotoNgo.setOnClickListener {
            getImage()
        }
        binding.imgEditNgoType.setOnClickListener {
            createDialog("Updating the Work categorization of NGO...", ngoType,"type")
        }
        binding.imgEditNgoNumber.setOnClickListener {
            createDialog("Update your Number" , ngoNumber, "number")
        }
        binding.imgEditNgoWebsite.setOnClickListener {
            createDialog("Changing the url of your website..", ngoWebsite, "website")
        }
        binding.imgBackBtnNgo.setOnClickListener {
            finish()
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
        dbRef.get().addOnCompleteListener {

            val ngoRegNum = it.result.child("uniqueId").value.toString() + "  "
            val ngoName = it.result.child("name").value.toString()+ "  "
            val ngoEmail = it.result.child("emailId").value.toString()+"  "
            ngoType = it.result.child("ngoType").value.toString()
            ngoNumber = it.result.child("phoneNo").value.toString()
            ngoWebsite = it.result.child("ngoWeb").value.toString()


            binding.tvRegNumNgo.text = ngoRegNum
            binding.tvNgoName.text = ngoName
            binding.tvEmailNgo.text = ngoEmail
            val type = "$ngoType  "
            binding.tvTypeNgo.text = type
            val number = "$ngoNumber  "
            binding.tvNumberNgo.text = number
            val website ="$ngoWebsite  "
            binding.tvWebsiteNgo.text = website
        }
    }

    private fun getImage(){
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.type ="image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image ")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
        startForResult.launch(chooserIntent)

    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result : ActivityResult ->
            if(result.resultCode == Activity.RESULT_OK){
                val uri = result.data?.data

                binding.progressBar3.visibility = View.VISIBLE

                val ref = storageRef.getReference("ngoImages/${auth.currentUser?.uid}.jpg")
                ref.putFile(uri!!).addOnSuccessListener {

                    it.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                        dbRef.child("ngoImage").setValue(uri.toString())
                    }

                    getImageFromFirebase()
                }.addOnFailureListener{
                    Toast.makeText(this, "Failed to upload the image !", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun createDialog(title : String , infoDetail: String , action : String){
        val mDialog = AlertDialog.Builder(this, androidx.appcompat.R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        val dialogView = layoutInflater.inflate(R.layout.update_dialog,null)
        mDialog.setView(dialogView)
        mDialog.setTitle(title)

        val alertDialog = mDialog.create()
        alertDialog.show()

        val info = dialogView.findViewById<EditText>(R.id.etUserDetail)
        val btnUpdate = dialogView.findViewById<Button>(R.id.btnUpdate)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        info.setText(infoDetail)

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnUpdate.setOnClickListener {

            when(action){
                "type" -> dbRef.child("ngoType").setValue(info.text.toString())
                "number" -> dbRef.child("phoneNo").setValue(info.text.toString())
                "website" -> dbRef.child("ngoWeb").setValue(info.text.toString())
            }
            Toast.makeText(this, "Successfully data updated !", Toast.LENGTH_SHORT).show()

            alertDialog.dismiss()
            getDataFromFirebase()

        }
    }

}