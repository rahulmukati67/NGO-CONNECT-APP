package com.shrushti.ngoconnectapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
        val ngoList = arrayListOf<Ngo_data>()
        ngoList.add(Ngo_data("1234","Abc","12345","abc@gmail.com","AnimalWelfare"))
        ngoList.add(Ngo_data("123466","Xyz","12345","abc@gmail.com","AnimalWelfare"))
        ngoList.add(Ngo_data("1234","Efg","12345","abc@gmail.com","AnimalWelfare"))
        ngoList.add(Ngo_data("1234","Hij","12345","abc@gmail.com","AnimalWelfare"))

        val rvNGOs = view.findViewById<RecyclerView>(R.id.rvNGOs)
        rvNGOs.layoutManager = LinearLayoutManager(context)
        rvNGOs.setHasFixedSize(true)
        ngoAdapter = NGOAdapter(ngoList)
        rvNGOs.adapter = ngoAdapter
    }

}