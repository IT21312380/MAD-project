package com.example.androidcartfirebase

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcartfirebase.activities.FetchingActivity1

import com.example.androidcartfirebase.activities.MainActivity1
import com.example.androidcartfirebase.adapter.MyDrinkAdapter
import com.example.androidcartfirebase.eventbus.UpdateCartEvent
import com.example.androidcartfirebase.listener.ICartLoadListner
import com.example.androidcartfirebase.listener.IDrinkLoadListener
import com.example.androidcartfirebase.model.Cartmodel
import com.example.androidcartfirebase.model.DrinkModel
import com.example.androidcartfirebase.utils.SpaceItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.nex3z.notificationbadge.NotificationBadge
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivityBuy : AppCompatActivity(), IDrinkLoadListener,ICartLoadListner {

    private lateinit var recycler_drink: RecyclerView
    private lateinit var mainLayout: View
    private lateinit var badge: NotificationBadge
    private lateinit var btnCart: FrameLayout
    private lateinit var Search: androidx.appcompat.widget.SearchView
    private lateinit var btnReview: ImageView
    private lateinit var btninsert :ImageView
    private lateinit var btnretriving :ImageView

    lateinit var drinkLoadListener: IDrinkLoadListener
    lateinit var cartLoadListener: ICartLoadListner
    private val mDatabase: DatabaseReference? = null

    lateinit var cusId:String

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
        countCartFromFirebase()
    }



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cusId = intent.getStringExtra("cusId") ?: ""


        btninsert = findViewById(R.id.insert)
        btnretriving= findViewById(R.id.insert2)

        btninsert.setOnClickListener {
            val intent = Intent(this@MainActivityBuy, InsertionActivity1::class.java)
            startActivity(intent)
        }
        btnretriving.setOnClickListener {
            val intent = Intent(this@MainActivityBuy, FetchingActivity1::class.java)
            startActivity(intent)
        }



        val img5: ImageView = findViewById(R.id.A1)
        img5.setOnClickListener {
            val intent = Intent(this@MainActivityBuy, Profile::class.java)

            val adapter = MyDrinkAdapter(this, ArrayList(), cartLoadListener, cusId)
            intent.putExtra("cusId", cusId)

            startActivity(intent)

        }

        cartLoadListener = this // Initialize the cartLoadListener property
        drinkLoadListener = this // Initialize the cartLoadListener property

        // Initialize the recycler_drink variable
        recycler_drink = findViewById(R.id.recycler_drink)

        // Set the layout manager for the RecyclerView
        recycler_drink.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with an empty list
        val adapter = MyDrinkAdapter(this, ArrayList(), this,cusId)

        // Set the adapter to the RecyclerView
        recycler_drink.adapter = adapter

        // Initialize the badge variable
        badge = findViewById(R.id.badge)
        // Initialize the mainLayout variable
        mainLayout = findViewById(android.R.id.content)

        Search = findViewById<androidx.appcompat.widget.SearchView>(R.id.search)

        btnReview = findViewById(R.id.btnreview)

        btnReview.setOnClickListener {
            val intent = Intent(this, MainActivity1::class.java)
            startActivity(intent)
        }


        countCartFromFirebase()
        loadDrinkFromFirebase()

        init()

    }

    private fun countCartFromFirebase() {
        val cartModels:MutableList<Cartmodel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Cart")

            .child(cusId )
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(cartSnapshot in snapshot.children)
                    {
                        val cartModel = cartSnapshot.getValue(Cartmodel::class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener.onLoadCartSuccess(cartModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener.onLoadCartFailed(error.message)
                }

            })
    }

    private fun loadDrinkFromFirebase() {
        val drinkModels : MutableList<DrinkModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Drink")
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                    {
                        for(drinkSnapshot in snapshot.children)
                        {
                            val drinkModel = drinkSnapshot.getValue(DrinkModel::class.java)
                            drinkModel!!.key = drinkSnapshot.key
                            drinkModels.add(drinkModel)
                        }
                        drinkLoadListener.onDrinkLoadSuccess(drinkModels)



                        Search.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                return false
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                // Filter the drinkModels list based on the query
                                val filteredList = drinkModels.filter { drinkModel ->
                                    drinkModel.name!!.contains(newText.orEmpty(), true)
                                }

                                // Create a new adapter with the filtered list and set it to the RecyclerView
                                cusId = intent.getStringExtra("cusId") ?: ""
                                val adapter = MyDrinkAdapter(applicationContext, filteredList, cartLoadListener,cusId)
                                recycler_drink.adapter = adapter

                                return true
                            }
                        })

                    }
                    else
                    {
                        drinkLoadListener.onDrinkLoadFailed("Vegetable Item not Exist")
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    drinkLoadListener.onDrinkLoadFailed(error.message)
                }

            })


    }


    private fun init(){
        drinkLoadListener = this
        cartLoadListener=this

        val gridlayoutManager =GridLayoutManager(this,2)
        recycler_drink.layoutManager = gridlayoutManager
        recycler_drink.addItemDecoration(SpaceItemDecoration())
        btnCart = findViewById(R.id.btnCart)
        btnCart.setOnClickListener{
            val intent = Intent(this@MainActivityBuy, CartActivity::class.java)
            intent.putExtra("cusId", cusId)
            startActivity(intent)
        }

    }

    override fun onDrinkLoadSuccess(drinkModelList: List<DrinkModel>?) {
        val cusId = intent.getStringExtra("cusId") ?: ""
        val adapter = MyDrinkAdapter(this,drinkModelList!!,cartLoadListener, cusId )
        recycler_drink.adapter= adapter
        adapter.notifyDataSetChanged()
    }

    override fun onDrinkLoadFailed(message: String?) {
        Snackbar.make(mainLayout,message!!,Snackbar.LENGTH_LONG).show()
    }

    override fun onLoadCartSuccess(cartModelList: List<Cartmodel>) {
        var cartSum = 0
        for (cartModel in cartModelList!!) cartSum+=cartModel!!.quantity

        badge!!.setNumber(cartSum)
    }

    override fun onLoadCartFailed(message: String?) {
        Snackbar.make(mainLayout,message!!,Snackbar.LENGTH_LONG).show()
    }


}
