package com.example.androidcartfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.example.androidcartfirebase.adapter.MyCartAdapter
import com.example.androidcartfirebase.eventbus.UpdateCartEvent
import com.example.androidcartfirebase.listener.ICartLoadListner
import com.example.androidcartfirebase.model.Cartmodel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class CartActivity : AppCompatActivity(), ICartLoadListner {
    var cartLoadListener:ICartLoadListner?=null
    private lateinit var recycler_cart: RecyclerView
    private lateinit var btnBack: ImageView
    private lateinit var txtTotal: TextView
    private lateinit var mainLayout: View


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        if(EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent::class.java))
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent::class.java)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onUpdateCartEvent(event: UpdateCartEvent)
    {
        loadCartFromFirebase()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        // Initialize recycler_cart here
        recycler_cart = findViewById(R.id.recycler_cart)
        //initialize
        btnBack = findViewById(R.id.btnBack)
        // Initialize txtTotal
        txtTotal = findViewById(R.id.txtTotal)

        init()
        loadCartFromFirebase()

    }

    private fun loadCartFromFirebase() {
        val cartModels:MutableList<Cartmodel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(cartSnapshot in snapshot.children)
                    {
                        val cartModel = cartSnapshot.getValue(Cartmodel::class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener!!.onLoadCartSuccess(cartModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener!!.onLoadCartFailed(error.message)
                }

            })
    }

    private fun init()
    {
        cartLoadListener=this
        val layoutManager = LinearLayoutManager(this)
        recycler_cart!!.layoutManager = layoutManager
        recycler_cart!!.addItemDecoration(DividerItemDecoration(this,layoutManager.orientation))
     btnBack!!.setOnClickListener {finish()}
    }

    override fun onLoadCartSuccess(cartModelList: List<Cartmodel>) {
var sum = 0.0
        for (cartModel in cartModelList!!)
        {
            sum+=cartModel!!.totalPrice
        }
        txtTotal.text = java.lang.StringBuilder("Total Rs. ").append(sum)
        val adapter = MyCartAdapter(this,cartModelList)
        recycler_cart!!.adapter = adapter


    }

    override fun onLoadCartFailed(message: String?) {
        Snackbar.make(mainLayout,message!!, Snackbar.LENGTH_LONG).show()
    }
}