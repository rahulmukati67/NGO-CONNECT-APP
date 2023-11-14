package com.project.ngoconnectapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.ngoconnectapp.databinding.ActivityNgoRegistrationPageBinding


class NgoRegistrationPage : AppCompatActivity() {

    private lateinit var binding: ActivityNgoRegistrationPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNgoRegistrationPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val database = Firebase.database
        val dbRef = database.getReference("ngoDetails")
        val auth = FirebaseAuth.getInstance()

        binding.btnRegAsNgo.setOnClickListener {
            val ngoRegId = binding.ngoRegId.text.toString()
            val ngoName = binding.ngoName.text.toString()
            val ngoContactNumber = binding.ngoContactNumber.text.toString()
            val ngoType = binding.ngoType.text.toString()
            val ngoWebsite = binding.ngoWebsite.text.toString()
            val ngoEmail = binding.ngoEmail.text.toString()
            val ngoPassword = binding.ngoPassword.text.toString()


            if (ngoRegId.isEmpty()) {
                binding.ngoRegId.error = "This field can't be empty"
            }
            else if (ngoName.isEmpty()) {
                binding.ngoName.error = "This field can't be empty"
            }
            else if (ngoContactNumber.isEmpty()) {
                binding.ngoContactNumber.error = "This field can't be empty"
            }
            else if (ngoType.isEmpty()) {
                binding.ngoType.error = "This field can't be empty"
            }
            else if (ngoWebsite.isEmpty()) {
                binding.ngoWebsite.error = "This field can't be empty"
            }
            else if (ngoEmail.isEmpty()) {
                binding.ngoEmail.error = "This field can't be empty"
            }
            else if (ngoPassword.isEmpty()) {
                binding.ngoPassword.error = "This field can't be empty"
            }
            else {
                val newNgoReg = Ngo_data(
                    ngoRegId,
                    ngoName,
                    ngoContactNumber,
                    ngoEmail,
                    ngoType,
                    ngoWebsite,
                    ngoPassword
                )
                auth.createUserWithEmailAndPassword(ngoEmail, ngoPassword).addOnCompleteListener {

                        if(it.isSuccessful){
                            Toast.makeText(this, "Registered Successfully" , Toast.LENGTH_SHORT).show()

                            auth.signInWithEmailAndPassword(ngoEmail,ngoPassword).addOnSuccessListener{
                                dbRef.child(auth.currentUser?.uid!!).setValue(newNgoReg)
                                val intent = Intent(this, MainActivity::class.java)
                                intent.putExtra("type", "ngo")
                                intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                            }.addOnFailureListener {exception ->
                                Toast.makeText(this, exception.message.toString(),Toast.LENGTH_SHORT).show()
                            }

                        }
                        else{
                            Toast.makeText(this , it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }

            }


        }


    }
}