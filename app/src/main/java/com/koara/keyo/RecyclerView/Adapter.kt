package com.koara.keyo.RecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.koara.keyo.Model.Entity
import com.koara.keyo.Fragments.*
import com.koara.keyo.R
import kotlinx.android.synthetic.main.saved_card.view.*
/*
    Setting feed from database
    Nothing to do here
 */
class Adapter : RecyclerView.Adapter<Adapter.MyViewHolder>() {
    private var credentialList = emptyList<Entity>()
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return  MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.saved_card, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = credentialList[position]
        holder.itemView.cardid.text = currentItem.id.toString()
        holder.itemView.site.text = currentItem.site.toString()
        holder.itemView.username.text = currentItem.username.toString()
        val str: String = "•".repeat(currentItem.password.toString().length)
        holder.itemView.password.text = str
        holder.itemView.cardLayout.setOnClickListener{
            val action = FeedFragmentDirections.actionFeedFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return credentialList.size
    }
    fun setData(entity : List<Entity>){
        this.credentialList = entity
        notifyDataSetChanged()
    }
}