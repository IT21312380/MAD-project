package com.example.androidcartfirebase.adapter

import android.app.AlertDialog
import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidcartfirebase.R
import com.example.androidcartfirebase.eventbus.UpdateCartEvent
import com.example.androidcartfirebase.model.Cartmodel
import com.google.firebase.database.FirebaseDatabase
import org.greenrobot.eventbus.EventBus

class MyCartAdapter (
    private val context: Context,
    private val cartModelList: List<Cartmodel>


        ):RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder>() {
    class MyCartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var btnMinus : ImageView? = null
        var btnPlus : ImageView? = null
        var imageView : ImageView? = null
        var btnDelete:ImageView?=null
        var txtName : TextView?=null
        var txtPrice : TextView?=null
        var txtQuantity : TextView?=null

        init {
            btnMinus =itemView.findViewById(R.id.btnMinus) as ImageView
            btnPlus =itemView.findViewById(R.id.btnPlus) as ImageView
            imageView =itemView.findViewById(R.id.imageView) as ImageView
            btnDelete =itemView.findViewById(R.id.btnDelete) as ImageView
            txtName =itemView.findViewById(R.id.txtName) as TextView
            txtPrice =itemView.findViewById(R.id.txtPrice) as TextView
            txtQuantity =itemView.findViewById(R.id.txtQuantity) as TextView



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartViewHolder {
        return MyCartViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.layout_cart_item,parent,false))
    }

    override fun getItemCount(): Int {
        return cartModelList.size
    }

    override fun onBindViewHolder(holder: MyCartViewHolder, position: Int) {
        Glide.with(context)
            .load(cartModelList[position].image)
            .into(holder.imageView!!)
        holder.txtName!!.text = StringBuilder().append(cartModelList[position].name)
        holder.txtPrice!!.text = StringBuilder("RS.").append(cartModelList[position].price)
        holder.txtQuantity!!.text = StringBuilder("").append(cartModelList[position].quantity)

        //event
        holder.btnMinus!!.setOnClickListener{_ ->minusCartItem(holder,cartModelList[position])}
        holder.btnPlus!!.setOnClickListener{_ ->plusCartItem(holder,cartModelList[position])}
        holder.btnDelete!!.setOnClickListener{_->
            val dialog=AlertDialog.Builder(context)
                .setTitle("Delete Item")
                .setMessage("Do you really want to delete item")
                .setNegativeButton("CANSEL"){dialog,_ ->dialog.dismiss()}
                .setPositiveButton("DELETE"){dialog,_ ->

                    notifyItemRemoved(position)
                    FirebaseDatabase.getInstance()
                        .getReference("Cart")
                        .child("UNIQUE_USER_ID")
                        .child(cartModelList[position].key!!)
                        .removeValue()
                        .addOnSuccessListener { EventBus.getDefault().postSticky(UpdateCartEvent()) }

                }
                .create()
            dialog.show()
        }
    }

    private fun plusCartItem(holder: MyCartAdapter.MyCartViewHolder, cartmodel: Cartmodel) {

        cartmodel.quantity+=1
        cartmodel.totalPrice= cartmodel.quantity*cartmodel.price!!.toFloat()
        holder.txtQuantity!!.text = StringBuilder("").append(cartmodel.quantity)
        updateFirebase(cartmodel)
    }


    private fun minusCartItem(holder: MyCartAdapter.MyCartViewHolder, cartmodel: Cartmodel) {

        if(cartmodel.quantity>1)
        {
            cartmodel.quantity-=1
            cartmodel.totalPrice= cartmodel.quantity*cartmodel.price!!.toFloat()
            holder.txtQuantity!!.text = StringBuilder("").append(cartmodel.quantity)
            updateFirebase(cartmodel)
        }
    }

    private fun updateFirebase(cartmodel: Cartmodel) {
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
            .child(cartmodel.key!!)
            .setValue(cartmodel)
            .addOnSuccessListener { EventBus.getDefault().postSticky(UpdateCartEvent()) }

    }

}