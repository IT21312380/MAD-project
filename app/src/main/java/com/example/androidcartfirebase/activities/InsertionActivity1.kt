package com.example.androidcartfirebase

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcartfirebase.activities.FetchingActivity1
import com.example.androidcartfirebase.activities.MainActivity1
import com.example.androidcartfirebase.model.DrinkModel
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity1 : AppCompatActivity() {


    private lateinit var name: EditText
    private lateinit var image: EditText
    private lateinit var price: EditText
    private lateinit var sellerName: EditText
    private lateinit var sellerdiscription: EditText
    private lateinit var noofunits: EditText
    private lateinit var saveButton: Button
    private lateinit var mobile: EditText
    private lateinit var btnBack: ImageView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion_1)

        btnBack = findViewById(R.id.btnBack)
        name = findViewById(R.id.etProductName)
        image = findViewById(R.id.imgupd)
        price = findViewById(R.id.etUnitPrice)
        sellerName = findViewById(R.id.etSellerName)
        sellerdiscription = findViewById(R.id.etdiscription)
        noofunits = findViewById(R.id.etnoofunits)
        saveButton = findViewById(R.id.btnSave)
        mobile = findViewById(R.id.mobile)

        saveButton.setOnClickListener {
            saveDataToFirebase()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

        }
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveDataToFirebase() {

        val name = name.text.toString().trim()
        val img = image.text.toString().trim()
        val price = price.text.toString().trim()
        val sellerName = sellerName.text.toString().trim()
        val sellerdiscription = sellerdiscription.text.toString().trim()
        val noofunits = noofunits.text.toString().trim()
        val mobile = mobile.text.toString().trim()

        val database = FirebaseDatabase.getInstance()
        val productsRef = database.getReference("Drink")

        val productId = productsRef.push().key
            ?: // Handle the error
            return

        val product = DrinkModel(
            name = name,
            image = img,
            price = price,
            sellerName = sellerName,
            sellerdiscription = sellerdiscription,
            noofunits = noofunits,
            mobile= mobile
        )

        // Retrieve the key of the existing node
        val existingKey = intent.getStringExtra("key")

        // Update the data at the existing key
        if (existingKey != null) {
            productsRef.child(existingKey).setValue(product)
                .addOnSuccessListener {
                    // Handle the success
                    Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    // Handle the failure
                    Toast.makeText(this, "Error saving data: " + e.message, Toast.LENGTH_SHORT).show()
                }
        }
    }
}
