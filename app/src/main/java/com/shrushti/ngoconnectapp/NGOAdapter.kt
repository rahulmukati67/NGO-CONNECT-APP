package com.shrushti.ngoconnectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NGOAdapter(private var ngoList: ArrayList<Ngo_data>) : RecyclerView.Adapter<NGOAdapter.NgoViewHolder>() {

    inner class NgoViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val name : TextView
        val  type : TextView
        init{
            name = itemView.findViewById(R.id.tv_ngo_name)
            type = itemView.findViewById(R.id.tv_ngo_type)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NGOAdapter.NgoViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.ngo_view_layout,parent,false)
        return NgoViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: NGOAdapter.NgoViewHolder, position: Int) {
        holder.name.text = ngoList[position].name
        holder.type.text = ngoList[position].ngoType
    }

    override fun getItemCount(): Int {
        return ngoList.size
    }

}