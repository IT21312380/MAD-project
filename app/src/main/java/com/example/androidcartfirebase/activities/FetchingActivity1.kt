package com.example.androidcartfirebase.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.example.androidcartfirebase.SellerDetailsActivity

import com.example.androidcartfirebase.R
import com.example.androidcartfirebase.SelAdapter
import com.example.androidcartfirebase.model.DrinkModel
import com.google.firebase.database.*



class FetchingActivity1 : AppCompatActivity() {

    private lateinit var  selRecyclerView: RecyclerView
    private lateinit var tvLoadData: TextView
    private lateinit var selList :ArrayList<DrinkModel>
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching_j)

        selRecyclerView = findViewById(R.id.rvsel)
        selRecyclerView.layoutManager = LinearLayoutManager(this)
        selRecyclerView.setHasFixedSize(true)
        tvLoadData=findViewById(R.id.tvLoadingData)

        selList = arrayListOf<DrinkModel>()

        getSellers()

    }

    private fun getSellers() {
        selRecyclerView.visibility = View.GONE
        tvLoadData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Drink")

        dbRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                selList.clear()
                if (snapshot.exists()) {
                    for (sellsnap in snapshot.children) {
                        val selData = sellsnap.getValue(DrinkModel::class.java)
                        selData?.let { sel ->
                            sel.image = sellsnap.child("image").value as? String
                            selList.add(sel)
                        }
                    }
                    val mAdapter = SelAdapter(selList)
                    selRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : SelAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val snapshot = snapshot.children.elementAt(position)
                            val key = snapshot.key

                            val intent = Intent(this@FetchingActivity1, SellerDetailsActivity::class.java)
                            intent.putExtra("key", key)
                            intent.putExtra("sellerName", selList[position].sellerName)
                            intent.putExtra("unitPrice", selList[position].price)
                            intent.putExtra("noofunits", selList[position].noofunits)
                            intent.putExtra("sellerdiscription", selList[position].sellerdiscription)
                            intent.putExtra("productName", selList[position].name)
                            selList[position].image?.let {
                                intent.putExtra("image", it)
                            }

                            startActivity(intent)
                        }
                    })

                    selRecyclerView.visibility = View.VISIBLE
                    tvLoadData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}
