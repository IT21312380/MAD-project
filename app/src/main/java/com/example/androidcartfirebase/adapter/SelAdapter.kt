package com.example.androidcartfirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcartfirebase.model.DrinkModel


class SelAdapter(private val selList: ArrayList<DrinkModel>) :
    RecyclerView.Adapter<SelAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.sel_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentSel = selList[position]
        holder.tvSelName.text = currentSel.sellerName
        holder.tvProductName.text = currentSel.name
        holder.tvUnitPrice.text = currentSel.price
    }

    override fun getItemCount(): Int {
        return selList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val tvSelName: TextView = itemView.findViewById(R.id.tvSelName)
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvUnitPrice: TextView = itemView.findViewById(R.id.tvUnitPrice)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}
