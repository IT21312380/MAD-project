package com.example.androidcartfirebase.adapters



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcartfirebase.R
import com.example.androidcartfirebase.models.CustomerComModel

class CusAdapter(private val cusList: ArrayList<CustomerComModel>) : RecyclerView.Adapter<CusAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_cuslist_item, parent, false)
        val tvCusName = itemView.findViewById<TextView>(R.id.tvCusName)
        return ViewHolder(itemView, tvCusName, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = cusList[position]
        holder.tvCusName.text = currentItem.cusName
    }

    override fun getItemCount(): Int {
        return cusList.size
    }

    class ViewHolder(itemView: View, val tvCusName: TextView, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}
