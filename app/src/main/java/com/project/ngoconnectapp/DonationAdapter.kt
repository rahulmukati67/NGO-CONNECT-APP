package com.project.ngoconnectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DonationAdapter (private var donationList : ArrayList<Donation_Data>) :
    RecyclerView.Adapter<DonationAdapter.DonationViewHolder>() {

    inner class DonationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
       val dAmount : TextView
       val dDate :TextView
       val dTime :TextView

       init {
           dAmount = itemView.findViewById(R.id.d_amount)
           dDate = itemView.findViewById(R.id.d_date)
           dTime = itemView.findViewById(R.id.d_Time)

       }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.donation_list_view,parent,false)
        return DonationViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return donationList.size
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        holder.dAmount.text = donationList[position].d_amount.toString()
        holder.dDate.text = donationList[position].d_date.toString()
        holder.dTime.text = donationList[position].d_time.toString()
    }
}