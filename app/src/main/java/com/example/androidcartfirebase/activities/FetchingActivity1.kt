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
import com.example.androidcartfirebase.model.InvenoryModel
import com.example.androidcartfirebase.R
import com.example.androidcartfirebase.SelAdapter
import com.google.firebase.database.*


class FetchingActivity1 : AppCompatActivity() {

    private lateinit var  selRecyclerView: RecyclerView
    private lateinit var tvLoadData: TextView
    private lateinit var selList :ArrayList<InvenoryModel>
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching_j)

        selRecyclerView = findViewById(R.id.rvsel)
        selRecyclerView.layoutManager = LinearLayoutManager(this)
        selRecyclerView.setHasFixedSize(true)
        tvLoadData=findViewById(R.id.tvLoadingData)

        selList = arrayListOf<InvenoryModel>()

        getSellers()

    }

    private fun getSellers()
    {
        selRecyclerView.visibility = View.GONE
        tvLoadData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("seller")

        dbRef.addValueEventListener(object : ValueEventListener{
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
               selList.clear()
                if(snapshot.exists())
                {

                    for(sellsnap in snapshot.children)
                    {
                 val selData = sellsnap.getValue(InvenoryModel::class.java)
                        selList.add(selData!!)
                    }
                    val mAdapter = SelAdapter(selList)
                    selRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object: SelAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                          val intent = Intent(this@FetchingActivity1, SellerDetailsActivity::class.java)

                            intent.putExtra("sellerName",selList[position].sellerName)
                            intent.putExtra("unitPrice",selList[position].unitPrice)
                            intent.putExtra("noofunits",selList[position].noofunits)
                            intent.putExtra("sellerdiscription",selList[position].sellerdiscription)
                            intent.putExtra("productName",selList[position].productName)


                            startActivity(intent)

                        }

                    })

                    selRecyclerView.visibility=View.VISIBLE
                    tvLoadData.visibility=View.GONE


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}