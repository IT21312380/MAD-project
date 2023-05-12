package com.example.androidcartfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import com.example.androidcartfirebase.activities.FetchingActivity1

import com.google.firebase.database.FirebaseDatabase

class SellerDetailsActivity : AppCompatActivity() {

    private lateinit var tvsellName: TextView
    private lateinit var tvnoofunits: TextView
    private lateinit var tvunitcost: TextView
    private lateinit var tvdiscription: TextView
    private lateinit var etproductname: TextView
    private lateinit var btnUpdate:Button
    private lateinit var btnDelete:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_details)

        initView()
        setValuesToView()

        val selId = intent.getStringExtra("key").toString()


        btnUpdate.setOnClickListener{
            openUpdateDialog(
                selId,
                intent.getStringExtra("sellerName").toString()
            )
        }
        btnDelete.setOnClickListener{
            deleteRecord(selId)
        }
    }

    private fun deleteRecord(
        id:String
    )
    {
        val dbRef = FirebaseDatabase.getInstance().getReference("Drink").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this,"Sellers data deleted",Toast.LENGTH_LONG).show()
            val intent = Intent(this, FetchingActivity1::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{error->
            Toast.makeText(this,"Deleting Err${error.message}",Toast.LENGTH_LONG).show()

        }
    }
    private fun initView() {
        tvsellName = findViewById(R.id.tvsellName)
        tvnoofunits = findViewById(R.id.tvnoofunits)
        tvunitcost = findViewById(R.id.tvunitcost)
        tvdiscription = findViewById(R.id.tvdiscription)
        etproductname = findViewById(R.id.etProductName)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)

    }
    private fun setValuesToView() {
        tvsellName.text = intent.getStringExtra("sellerName")
        tvunitcost.text = intent.getStringExtra("unitPrice")
        tvnoofunits.text = intent.getStringExtra("noofunits")
        tvdiscription.text = intent.getStringExtra("sellerdiscription")
        etproductname.text = intent.getStringExtra("productName")

        // get the Firebase Storage reference for the image

    }

    private fun openUpdateDialog(
        selId:String,
        empName:String,
    )
    {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog1,null)
        mDialog.setView(mDialogView)

        val etSelName = mDialogView.findViewById<EditText>(R.id.etSellname)
        val noofunits = mDialogView.findViewById<EditText>(R.id.noofunits)
        val unitcost = mDialogView.findViewById<EditText>(R.id.unitcost)
        val discription = mDialogView.findViewById<EditText>(R.id.discription)
        val productname = mDialogView.findViewById<EditText>(R.id.productname)
        val btnupdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etSelName.setText( intent.getStringExtra("sellerName").toString())
        noofunits.setText( intent.getStringExtra("unitPrice").toString())
        unitcost.setText( intent.getStringExtra("noofunits").toString())
        discription.setText( intent.getStringExtra("sellerdiscription").toString())
        productname.setText( intent.getStringExtra("productName").toString())

        mDialog.setTitle("Updating $empName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnupdateData.setOnClickListener{
            updateSellData(
                selId,
                etSelName.text.toString(),
                noofunits.text.toString(),
                unitcost.text.toString(),
                discription.text.toString(),
                productname.text.toString()
            )
            Toast.makeText(applicationContext,"Seller Data Updated",Toast.LENGTH_LONG).show()

            tvsellName.text = etSelName.text.toString()
            tvunitcost.text = noofunits.text.toString()
            tvnoofunits.text = unitcost.text.toString()
            tvdiscription.text = discription.text.toString()
            etproductname.text = productname.text.toString()

            alertDialog.dismiss()
        }

    }
    private fun updateSellData(
        selId: String,
        selName: String,
        noOfUnits: String,
        unitCost: String,
        description: String,
        productName: String
    ) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Drink").child(selId)

        myRef.child("sellerName").setValue(selName)
        myRef.child("noofunits").setValue(noOfUnits)
        myRef.child("unitPrice").setValue(unitCost)
        myRef.child("sellerdiscription").setValue(description)
        myRef.child("productName").setValue(productName)
    }

}