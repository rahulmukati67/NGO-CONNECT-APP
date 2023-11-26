package com.project.ngoconnectapp

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.project.ngoconnectapp.databinding.ActivityMyDonationsBinding
import java.time.LocalDate
import java.time.LocalTime

class MyDonationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyDonationsBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyDonationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        val dbRef = database.getReference("Donations").child(auth.currentUser?.uid!!)


        binding.btnDonate.setOnClickListener {
            val amount ="Rs. " + binding.donationAmount.text.toString() + " /-"
            val time = " ${LocalTime.now().hour} : ${LocalTime.now().minute} : ${LocalTime.now().second} "
            val date = LocalDate.now().toString()
            val donate = Donation_Data(amount, date, time)

            dbRef.child("$date $time").setValue(donate)
                .addOnCompleteListener { task ->
                    val builder = AlertDialog.Builder(this@MyDonationsActivity)

                    if (task.isSuccessful) {

                        builder.setTitle("Thanks")
                        builder.setMessage("You have Successfully Donated : Rs. $amount")
                        builder.setPositiveButton("OK") { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                    } else {

                        builder.setTitle("Error")
                        builder.setMessage("Something went wrong please try again later")
                        builder.setPositiveButton("OK") { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                    }

                    val alertDialog = builder.create()
                    alertDialog.show()
                    alertDialog.window?.setBackgroundDrawable(
                        ColorDrawable(ContextCompat.getColor(this, R.color.white )
                        )
                    )
                }
        }

        binding.btnDonateList.setOnClickListener {
            val intent = Intent(this, DonationList::class.java)
            startActivity(intent)
        }
    }
}
