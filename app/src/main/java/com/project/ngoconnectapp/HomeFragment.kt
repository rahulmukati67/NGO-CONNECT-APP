package com.project.ngoconnectapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var ngoAdapter: NGOAdapter
    private lateinit var progressBar : ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        progressBar = view.findViewById(R.id.progressBar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvNGOs = view.findViewById<RecyclerView>(R.id.rvNGOs)
        rvNGOs.layoutManager = LinearLayoutManager(context)
        rvNGOs.setHasFixedSize(true)
        progressBar.visibility = View.VISIBLE

        val database = Firebase.database
        val dbRef =  database.getReference("ngoDetails")

        val ngoList = arrayListOf<Ngo_data>()

        ngoAdapter = NGOAdapter(ngoList)

        ngoAdapter.setItemClickListener(object : NGOAdapter.OnItemClickListener{
            override fun onClick(position: Int) {
                val intent = Intent(activity, NGODetailPage::class.java)
                intent.putExtra("name", ngoList[position].name)
                intent.putExtra("type", ngoList[position].ngoType)
                intent.putExtra("phoneNo", ngoList[position].phoneNo)
                intent.putExtra("email", ngoList[position].emailId)
                intent.putExtra("website",ngoList[position].ngoWeb)
                startActivity(intent)
            }

        })
        rvNGOs.adapter = ngoAdapter

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                ngoList.clear()
                if(snapshot.exists()){
                    for(ngoSnap in snapshot.children){
                        val ngoData = ngoSnap.getValue(Ngo_data::class.java)
                        ngoList.add(ngoData!!)
                    }
                    progressBar.visibility = View.GONE
                    ngoAdapter = NGOAdapter(ngoList)

                    ngoAdapter.setItemClickListener(object : NGOAdapter.OnItemClickListener{
                        override fun onClick(position: Int) {
                            val intent = Intent(activity, NGODetailPage::class.java)
                            intent.putExtra("name", ngoList[position].name)
                            intent.putExtra("type", ngoList[position].ngoType)
                            intent.putExtra("phoneNo", ngoList[position].phoneNo)
                            intent.putExtra("email", ngoList[position].emailId)
                            intent.putExtra("website",ngoList[position].ngoWeb)
                            startActivity(intent)
                        }

                    })
                    rvNGOs.adapter = ngoAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message,Toast.LENGTH_SHORT).show()
            }

        })








    }

}