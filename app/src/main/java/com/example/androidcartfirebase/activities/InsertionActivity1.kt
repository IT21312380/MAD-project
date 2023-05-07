package com.example.androidcartfirebase

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.androidcartfirebase.model.InvenoryModel
import com.example.androidcartfirebase.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity1 : AppCompatActivity() {

    private lateinit var etSellerName : EditText
    private lateinit var etdiscription : EditText
    private lateinit var etProductName : EditText
    private lateinit var etUnitPrice : EditText
    private lateinit var etnoofunits : EditText
    private lateinit var btnsave : Button

    private  lateinit var dbRef : DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion_j)


        etSellerName = findViewById(R.id.etSellerName)
        etdiscription = findViewById(R.id.etdiscription)
        etProductName = findViewById(R.id.etProductName)
        etUnitPrice = findViewById(R.id.etUnitPrice)
        etnoofunits = findViewById(R.id.etnoofunits)
        btnsave = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("seller")

        btnsave.setOnClickListener{
            saveSellerData()
        }

    }
    private fun saveSellerData(){
        val sellerName = etSellerName.text.toString()
        val sellerdiscription = etdiscription.text.toString()
        val productName = etProductName.text.toString()
        val unitPrice = etUnitPrice.text.toString()
        val noofunits = etnoofunits.text.toString()

        if(sellerName.isEmpty())
        {
            etSellerName.error = "please enter location"
        }
        if(sellerdiscription.isEmpty())
        {
            etdiscription.error = "please enter date"
        }
        if(productName.isEmpty())
        {
            etProductName.error = "please enter Time"
        }
        if(unitPrice.isEmpty())
        {
            etUnitPrice.error = "please enter phone"
        }
        if(noofunits.isEmpty())
        {
            etnoofunits.error = "please enter location"
        }

        val selId = dbRef.push().key!!

        val seller = InvenoryModel(selId,sellerName,sellerdiscription,productName,unitPrice,noofunits)

        dbRef.child(selId).setValue(seller)
            .addOnCompleteListener{
                Toast.makeText(this,"Data inserted",Toast.LENGTH_LONG).show()

                etSellerName.text.clear()
                etdiscription.text.clear()
                etProductName.text.clear()
                etUnitPrice.text.clear()
                etnoofunits.text.clear()


            }.addOnFailureListener{err ->
                Toast.makeText(this,"Error ${err.message}",Toast.LENGTH_LONG).show()
            }
    }
}