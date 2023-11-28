package com.project.ngoconnectapp

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.project.ngoconnectapp.databinding.ActivityMyDonationsBinding
import java.time.LocalDate
import java.time.LocalTime

class MyDonationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyDonationsBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyDonationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        dbRef = database.getReference("Donations").child(auth.currentUser?.uid!!)


        binding.btnDonate.setOnClickListener {

            val amountToPay = binding.donationAmount.text.toString()
            if (amountToPay.isNotEmpty()) {
                val uri = Uri.parse("upi://pay").buildUpon()
                    .appendQueryParameter("pa","shrushtinagar220@okicici")
                    .appendQueryParameter("pn", "Shrushti Nagar")
                    .appendQueryParameter("mc", "")
                    .appendQueryParameter("tr", "25584584")
                    .appendQueryParameter("tn", "Donation")
                    .appendQueryParameter("am", amountToPay)
                    .appendQueryParameter("cu", "INR")
                    .build()
                val intent = Intent(Intent.ACTION_VIEW, uri)

                val chooser = Intent.createChooser(intent, "Pay with :")
                if (chooser.resolveActivity(packageManager) != null) {
                    startForActivityResult.launch(intent)
                } else {
                    Toast.makeText(
                        this,
                        "No UPI app found , please install one to continue..",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else{
                binding.donationAmount.error = "Please Enter the amount !"
            }



        }

        binding.btnDonateList.setOnClickListener {
            val intent = Intent(this, DonationList::class.java)
            startActivity(intent)
        }
        binding.cardBackBtn.setOnClickListener {
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val startForActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(Activity.RESULT_OK == result.resultCode){

            val amount = "Rs. ${binding.donationAmount.text.toString()} /-"
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
        else {
            Toast.makeText(this , "Donation Unsuccessful , Please Try Again !", Toast.LENGTH_SHORT).show()
        }

        binding.donationAmount.text!!.clear()
    }
}
