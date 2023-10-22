package com.project.ngoconnectapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvNGOs = view.findViewById<RecyclerView>(R.id.rvNGOs)
        rvNGOs.layoutManager = LinearLayoutManager(context)
        rvNGOs.setHasFixedSize(true)

        val database = Firebase.database
        val dbRef =  database.getReference("ngoDetails")

        val ngoList = arrayListOf<Ngo_data>()
        val ngoData1 = Ngo_data("1234","Abc","12345","abc@gmail.com","AnimalWelfare","www.abc.com")
        val ngoData2 = Ngo_data("123466","Xyz","12345","abc@gmail.com","AnimalWelfare","www.xyz.com")
        val ngoData3 = Ngo_data("12345689","Efg","12345","abc@gmail.com","AnimalWelfare","www.efg.com")
        val ngoData4 = Ngo_data("12343333","Hij","12345","abc@gmail.com","AnimalWelfare","www.hij.com")

        dbRef.child(ngoData1.uniqueId.toString()).setValue(ngoData1)
        dbRef.child(ngoData2.uniqueId.toString()).setValue(ngoData2)
        dbRef.child(ngoData3.uniqueId.toString()).setValue(ngoData3)
        dbRef.child(ngoData4.uniqueId.toString()).setValue(ngoData4)

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                ngoList.clear()
                if(snapshot.exists()){
                    for(ngoSnap in snapshot.children){
                        val ngoData = ngoSnap.getValue(Ngo_data::class.java)
                        ngoList.add(ngoData!!)
                    }
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
                Toast.makeText(context,"${error.message}",Toast.LENGTH_SHORT).show()
            }

        })








    }

}