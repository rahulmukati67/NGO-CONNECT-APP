package com.project.ngoconnectapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var ngoAdapter: NGOAdapter
    private lateinit var rvNGOs: RecyclerView
    private lateinit var ngoList: ArrayList<Ngo_data>
    private lateinit var filterList: ArrayList<Ngo_data>
    private lateinit var db :FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var obj: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvNGOs = view.findViewById(R.id.rvNGOs)
        rvNGOs.layoutManager = LinearLayoutManager(context)
        rvNGOs.setHasFixedSize(true)

        db = Firebase.database
        dbRef = db.getReference("ngoDetails")

        ngoList = arrayListOf<Ngo_data>()
        filterList = arrayListOf<Ngo_data>()


        filteredItem("")


    }

    private fun filteredItem(s: String) {
        dbRef = db.getReference("ngoDetails")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ngoList.clear()
                filterList.clear()
                if (snapshot.exists()) {
                    for (ngoSnap in snapshot.children) {
                        val ngoData = ngoSnap.getValue(Ngo_data::class.java)
                        ngoList.add(ngoData!!)
                        if (s.isNullOrEmpty()) {
                            filterList.add(ngoData)
                        } else if (ngoData!!.name.toString().lowercase().contains(s.lowercase()) ||
                            ngoData!!.ngoType.toString().lowercase().contains(s)
                        ) {
                            filterList.add(ngoData)
                        }
                    }

                    ngoAdapter = NGOAdapter(filterList)

                    ngoAdapter.setItemClickListener(object : NGOAdapter.OnItemClickListener {
                        override fun onClick(position: Int) {
                            val intent = Intent(activity, NGODetailPage::class.java)
                            intent.putExtra("name", ngoList[position].name)
                            intent.putExtra("type", ngoList[position].ngoType)
                            intent.putExtra("phoneNo", ngoList[position].phoneNo)
                            intent.putExtra("email", ngoList[position].emailId)
                            intent.putExtra("website", ngoList[position].ngoWeb)
                            intent.putExtra("image", ngoList[position].ngoImage)
                            startActivity(intent)
                        }

                    })
                    rvNGOs.adapter = ngoAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity?.applicationContext, error.message, Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    fun itemFun(args: Bundle) {
        val str = args.getString("str", "a").toString()
        abc(str)
    }

    fun abc(s: String) {
        try {
//            if (isAdded) {
//                Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
//
//            } else {
//                Thread.sleep(200)
//                Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
//            }
            if(::db.isInitialized){
                filteredItem(s)
            }


        } catch (e: Exception) {
            Log.i("str", e.message.toString())
        }

    }


}