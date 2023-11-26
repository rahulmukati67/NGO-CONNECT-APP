package com.project.ngoconnectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class NGOAdapter(private var ngoList: ArrayList<Ngo_data>) : RecyclerView.Adapter<NGOAdapter.NgoViewHolder>() {
    private lateinit var mListener : OnItemClickListener

    inner class NgoViewHolder(itemView:View, listener : OnItemClickListener) : RecyclerView.ViewHolder(itemView){

        val name : TextView
        val  type : TextView
        val imgNgo : ImageView
        private val cardView : com.google.android.material.card.MaterialCardView
        init{
            name = itemView.findViewById(R.id.tv_ngo_name)
            type = itemView.findViewById(R.id.tv_ngo_type)
            cardView = itemView.findViewById(R.id.cvNgoDetail)
            imgNgo = itemView.findViewById(R.id.img_ngo)
            cardView.setOnClickListener {
                listener.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NgoViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.ngo_view_layout,parent,false)
        return NgoViewHolder(inflater, mListener)
    }

    override fun onBindViewHolder(holder: NgoViewHolder, position: Int) {
        holder.name.text = ngoList[position].name
        holder.type.text = ngoList[position].ngoType
        val imgUri = ngoList[position].ngoImage
        if(imgUri != null){
            Picasso.get().load(ngoList[position].ngoImage).into(holder.imgNgo)
        }

    }
    fun updateList(newList: List<Ngo_data>) {
        ngoList = newList as ArrayList<Ngo_data>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return ngoList.size
    }

    interface OnItemClickListener{
        fun onClick(position:Int)
    }
    fun setItemClickListener(listener : OnItemClickListener){
        mListener = listener
    }

}