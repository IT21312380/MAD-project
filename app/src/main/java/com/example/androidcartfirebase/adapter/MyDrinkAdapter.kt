package com.example.androidcartfirebase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcartfirebase.model.DrinkModel
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.androidcartfirebase.R
import com.example.androidcartfirebase.eventbus.UpdateCartEvent
import com.example.androidcartfirebase.listener.ICartLoadListner
import com.example.androidcartfirebase.listener.IRecyclerClickListner
import com.example.androidcartfirebase.model.Cartmodel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.greenrobot.eventbus.EventBus

class MyDrinkAdapter (
    private val context: Context,
    private var list:List<DrinkModel>,
    private val cartListner: ICartLoadListner

    ): RecyclerView.Adapter<MyDrinkAdapter.MyDrinkViewHolder>() {

    class MyDrinkViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

         var imageView: ImageView?=null
        var txtName: TextView?=null
         var txtPrice:TextView?=null

        private var clickListner : IRecyclerClickListner? = null

        fun setClickListner(clickListner: IRecyclerClickListner)
        {
            this.clickListner=clickListner;
        }
        init {
            imageView= itemView.findViewById(R.id.imageView) as ImageView
            txtName= itemView.findViewById(R.id.txtName) as TextView
            txtPrice= itemView.findViewById(R.id.txtPrice) as TextView

            itemView.setOnClickListener(this)


        }

        override fun onClick(v: View?) {
        clickListner!!.onItemClicklistner(v,adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDrinkViewHolder {
      return MyDrinkViewHolder(LayoutInflater.from(context)
          .inflate(R.layout.layout_drink_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size

    }

    fun searchDataList(searchList: List<DrinkModel>)
    {
        list=searchList
        notifyDataSetChanged()
    }



    override fun onBindViewHolder(holder: MyDrinkViewHolder, position: Int) {
       Glide.with(context)
           .load(list[position].image)
           .into(holder.imageView!!)
        holder.txtName!!.text = StringBuilder().append(list[position].name)
        holder.txtPrice!!.text = StringBuilder("Rs. ").append(list[position].price)

        holder.setClickListner(object:IRecyclerClickListner{
            override fun onItemClicklistner(view: View?, position: Int) {
                addToCart(list[position])
            }

        })
    }

    private fun addToCart(drinkModel: DrinkModel) {
        val userCart = FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")// here is user id, you can use afirebase auth uid here

        userCart.child(drinkModel.key!!)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   if(snapshot.exists())//if item already in the cart just update
                   {
                       val cartmodel=snapshot.getValue(Cartmodel::class.java)
                       val updateData: MutableMap<String,Any> = HashMap()
                       cartmodel!!.quantity=cartmodel!!.quantity+1;
                       updateData["quantity"] = cartmodel!!.quantity
                       updateData["totalPrice"] = cartmodel!!.quantity* cartmodel.price!!.toFloat()

                       userCart.child(drinkModel.key!!)
                           .updateChildren(updateData)
                           .addOnSuccessListener {
                               EventBus.getDefault().postSticky(UpdateCartEvent())
                               cartListner.onLoadCartFailed( "Success add to cart")
                           }
                           .addOnFailureListener{e -> cartListner.onLoadCartFailed(e.message)}


                   }else{

                       val cartmodel = Cartmodel()
                       cartmodel.key = drinkModel.key
                       cartmodel.name = drinkModel.name
                       cartmodel.image = drinkModel.image
                       cartmodel.price = drinkModel.price
                       cartmodel.quantity=1
                       cartmodel.totalPrice = drinkModel.price!!.toFloat()

                       userCart.child(drinkModel.key!!)
                           .setValue(cartmodel)
                           .addOnSuccessListener {
                               EventBus.getDefault().postSticky(UpdateCartEvent())
                               cartListner.onLoadCartFailed("Success add to cart")

                           }
                           .addOnFailureListener { e ->cartListner.onLoadCartFailed(e.message) }

                   }
                }

                override fun onCancelled(error: DatabaseError) {
                    cartListner.onLoadCartFailed(error.message)
                }

            } )

    }




}