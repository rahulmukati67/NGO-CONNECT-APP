package com.project.ngoconnectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DonationAdapter (private var DonationList :ArrayList<Donation_Data>) :
    RecyclerView.Adapter<DonationAdapter.DonationViewHolder>() {

    inner class DonationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
       val D_amount : TextView
       val D_date :TextView
       val D_time :TextView

       init {
           D_amount = itemView.findViewById(R.id.d_amount)
           D_date = itemView.findViewById(R.id.d_date)
           D_time = itemView.findViewById(R.id.d_Time)

       }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.donation_list_view,parent,false)
        return DonationViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return DonationList.size
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        holder.D_amount.text = DonationList[position].D_amount
        holder.D_date.text = DonationList[position].D_date.toString()
        holder.D_time.text = DonationList[position].D_time.toString()
    }
}