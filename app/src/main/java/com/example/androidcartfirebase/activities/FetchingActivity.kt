package com.example.androidcartfirebase.activities



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcartfirebase.R
import com.example.androidcartfirebase.adapters.CusAdapter
import com.example.androidcartfirebase.models.CustomerComModel
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot

class FetchingActivity : AppCompatActivity() {

    private lateinit var cusRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var cusList: ArrayList<CustomerComModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        cusRecyclerView = findViewById(R.id.rvCus)
        cusRecyclerView.layoutManager = LinearLayoutManager(this)
        cusRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        cusList = arrayListOf<CustomerComModel>()

        getCustomerData()

    }

    private fun getCustomerData(){
        cusRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("CustomerCom")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                cusList.clear()
                if (snapshot.exists()){
                    for (cusSnap in snapshot.children){
                        val cusData = cusSnap.getValue(CustomerComModel::class.java)

                        cusList.add(cusData!!)
                    }
                    val mAdapter = CusAdapter(cusList)
                    cusRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : CusAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@FetchingActivity, CusDetails::class.java)

                            //put extras
                            intent.putExtra("cusId", cusList[position].cusId)
                            intent.putExtra("cusName", cusList[position].cusName)
                            intent.putExtra("cusComment", cusList[position].cusComment)
                            intent.putExtra("rating", cusList[position].rating)

                            startActivity(intent)
                        }
                    })


                    cusRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FetchingActivity", "Error fetching customer data: $error")
            }

        })
    }
}
