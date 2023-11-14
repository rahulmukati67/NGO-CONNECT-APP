package com.project.ngoconnectapp

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.project.ngoconnectapp.databinding.ActivityMyDonationsBinding

class MyDonationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyDonationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyDonationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardBackBtn.setOnClickListener {
            finish()
        }

        binding.btnDonate.setOnClickListener{
            val builder = AlertDialog.Builder(this@MyDonationsActivity)
            builder.setTitle("Thanks")
            builder.setMessage("You have Successfully Donated : Rs. " + binding.donationAmount.text.toString())
            builder.setPositiveButton("OK" ) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            val alertDialog = builder.create()
            alertDialog.show()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.white)))

        }



    }
}