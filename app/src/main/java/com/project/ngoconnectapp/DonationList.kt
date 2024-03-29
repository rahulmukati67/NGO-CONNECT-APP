package com.project.ngoconnectapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DonationList : AppCompatActivity() {
    private  lateinit var donationAdapter :DonationAdapter
    private lateinit var auth :FirebaseAuth

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_list)

        auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val dbRef =  database.getReference("Donations").child(auth.currentUser?.uid.toString())

        val donationList = arrayListOf<Donation_Data>()


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val tvEmpty = findViewById<TextView>(R.id.tvEmpty)

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                donationList.clear()
                if (snapshot.exists()) {
                    for (e in snapshot.children) {
                        val donation = e.getValue(Donation_Data::class.java)
                        donationList.add(donation!!)
                    }
                    donationAdapter = DonationAdapter(donationList)
                    recyclerView.adapter = donationAdapter
                }
                if(donationList.isEmpty()){
                    tvEmpty.visibility = View.VISIBLE
                }
                else{
                    tvEmpty.visibility = View.INVISIBLE
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}