package com.project.ngoconnectapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class NgoRegistrationPage : AppCompatActivity() {

    private lateinit var ngoRegId: EditText
    private lateinit var ngoName: EditText
    private lateinit var ngoContactNumber: EditText
    private lateinit var ngoType: EditText
    private lateinit var ngoWebsite: EditText
    private lateinit var ngoEmail: EditText
    private lateinit var ngoPassword: EditText
    private lateinit var btnRegAsNgo: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ngo_registration_page)

        ngoName = findViewById(R.id.ngoName)
        ngoRegId = findViewById(R.id.ngoRegId)
        ngoContactNumber = findViewById(R.id.ngoContactNumber)
        ngoType = findViewById(R.id.ngoType)
        ngoWebsite = findViewById(R.id.ngoWebsite)
        ngoEmail = findViewById(R.id.ngoEmail)
        ngoPassword = findViewById(R.id.ngoPassword)
        btnRegAsNgo = findViewById(R.id.btnRegAsNgo)

        val database = Firebase.database
        val dbRef = database.getReference("ngoDetails")

        btnRegAsNgo.setOnClickListener {
            val ngoName = ngoName.text.toString()
            val ngoRegId = ngoRegId.text.toString()
            val ngoContactNumber = ngoContactNumber.text.toString()
            val ngoType = ngoType.text.toString()
            val ngoWebsite = ngoWebsite.text.toString()
            val ngoEmail = ngoEmail.text.toString()
            val ngoPassword = ngoPassword.text.toString()


            val newNgoReg = Ngo_data(
                ngoRegId,
                ngoName,
                ngoContactNumber,
                ngoEmail,
                ngoType,
                ngoWebsite,
                ngoPassword
            )
            dbRef.child(newNgoReg.uniqueId.toString()).setValue(newNgoReg)

            val intent = Intent(this@NgoRegistrationPage, MainActivity::class.java)
            finish()
            startActivity(intent)

        }


    }
}