package com.example.androidcartfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Profile : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var etPBtnUpdate: Button
    private lateinit var etPBtnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        etPBtnDelete = findViewById(R.id.etPBtnDelete) // initialize the button
        etPBtnUpdate = findViewById(R.id.etPBtnUpdate)

        val cusId = intent.getStringExtra("cusId") ?: ""
        val password = intent.getStringExtra("password") ?: ""

        if (cusId.isBlank()) {
            Toast.makeText(this, "Customer ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        dbRef = FirebaseDatabase.getInstance().getReference("Customers").child(cusId)

        val etPName: EditText = findViewById(R.id.etPName)
        val etPNIC: EditText = findViewById(R.id.etPNIC)
        val erPAddress: EditText = findViewById(R.id.erPAddress)
        val etPPhoneNumber: EditText = findViewById(R.id.etPPhoneNumber)
        val etPEmail: EditText = findViewById(R.id.etPEmail)
        val etPPassword: EditText = findViewById(R.id.etPPassword)

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val customer = snapshot.getValue(RegistrationModel::class.java)
                    if (customer != null) {
                        etPName.setText(customer.name)
                        etPNIC.setText(customer.nic)
                        erPAddress.setText(customer.address)
                        etPPhoneNumber.setText(customer.phoneNumber)
                        etPEmail.setText(customer.email)
                        etPPassword.setText(customer.password)
                    } else {
                        Toast.makeText(this@Profile, "Customer data not found", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@Profile, "Customer ID not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Profile, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e("Profile", "Firebase error", error.toException())
                finish()
            }
        })

        etPBtnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("cusId").toString()
            )
        }

        etPBtnUpdate.setOnClickListener {
            val name = etPName.text.toString()
            val nic = etPNIC.text.toString()
            val address = erPAddress.text.toString()
            val phoneNumber = etPPhoneNumber.text.toString()
            val email = etPEmail.text.toString()
            val password = etPPassword.text.toString()

            val intent = Intent(this, UpdateProfile::class.java)
            intent.putExtra("cusId", cusId)
            intent.putExtra("name", name)
            intent.putExtra("nic", nic)
            intent.putExtra("address", address)
            intent.putExtra("phoneNumber", phoneNumber)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            startActivity(intent)
        }
    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Customers").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Employee data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, welcomelog::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}