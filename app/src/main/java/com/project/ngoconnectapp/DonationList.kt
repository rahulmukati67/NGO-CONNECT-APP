package com.project.ngoconnectapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DonationList : AppCompatActivity() {
    private  lateinit var DonationAdapter :DonationAdapter
    private lateinit var auth :FirebaseAuth

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_list)

        val database = Firebase.database
        auth = FirebaseAuth.getInstance()
        val dbRef =  database.getReference("Donations").child(auth.currentUser?.uid!!)

        val DonationList = arrayListOf<Donation_Data>()
        val donationAdapter = DonationAdapter(DonationList)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = donationAdapter

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                DonationList.clear()
                if (snapshot.exists()) {
                    for (e in snapshot.children) {
                        val donation = e.getValue(Donation_Data::class.java)
                        DonationList.add(donation!!)
                    }
                    DonationAdapter.notifyDataSetChanged()
            }
    }

            override fun onCancelled(error: DatabaseError) {

            }

        })
}
}