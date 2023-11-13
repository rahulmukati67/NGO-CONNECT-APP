package com.project.ngoconnectapp

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.project.ngoconnectapp.databinding.ActivityMyDonationsBinding

class MyDonationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyDonationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyDonationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDonate.setOnClickListener{
            val builder = AlertDialog.Builder(this@MyDonationsActivity)
            builder.setTitle("Thanks").setMessage("You Have Successful Donated Amount : Rs " + binding.donationAmount.text.toString())
            builder.setPositiveButton("OK" ) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            val alertDialog = builder.create()
            alertDialog.show()

        }



    }
}