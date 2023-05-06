package com.example.androidcartfirebase

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateProfile : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var cusId: String

    private lateinit var etPUName: EditText
    private lateinit var etPUNIC: EditText
    private lateinit var etPUAddress: EditText
    private lateinit var etPUPhoneNumber: EditText
    private lateinit var etPUEmail: EditText
    private lateinit var etPUPassword: EditText
    private lateinit var etPUBtnUpdate: Button

    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        cusId = intent.getStringExtra("cusId") ?: ""
        if (cusId.isBlank()) {
            Toast.makeText(this, "Customer ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        dbRef = FirebaseDatabase.getInstance().getReference("Customers").child(cusId)

        etPUName = findViewById(R.id.etPUName)
        etPUNIC = findViewById(R.id.etPUNIC)
        etPUAddress = findViewById(R.id.etPUAddress)
        etPUPhoneNumber = findViewById(R.id.etPUPhoneNumber)
        etPUEmail = findViewById(R.id.etPUEmail)
        etPUPassword = findViewById(R.id.etPUPassword)

        etPUName.setText(intent.getStringExtra("name"))
        etPUNIC.setText(intent.getStringExtra("nic"))
        etPUAddress.setText(intent.getStringExtra("address"))
        etPUPhoneNumber.setText(intent.getStringExtra("phoneNumber"))
        etPUEmail.setText(intent.getStringExtra("email"))
        etPUPassword.setText(intent.getStringExtra("password"))

        etPUBtnUpdate = findViewById(R.id.etPUBtnUpdate)
        etPUBtnUpdate.setOnClickListener {
            saveRecord()
        }
    }

    private fun saveRecord() {
        val name = etPUName.text.toString().trim()
        val nic = etPUNIC.text.toString().trim()
        val address = etPUAddress.text.toString().trim()
        val phoneNumber = etPUPhoneNumber.text.toString().trim()
        val email = etPUEmail.text.toString().trim()
        val password = etPUPassword.text.toString().trim()

        if (name.isEmpty()) {
            etPUName.error = "Name is required"
            etPUName.requestFocus()
            return
        }

        if (nic.isEmpty()) {
            etPUNIC.error = "NIC is required"
            etPUNIC.requestFocus()
            return
        }

        if (address.isEmpty()) {
            etPUAddress.error = "Address is required"
            etPUAddress.requestFocus()
            return
        }

        if (phoneNumber.isEmpty()) {
            etPUPhoneNumber.error = "Phone number is required"
            etPUPhoneNumber.requestFocus()
            return
        }

        if (email.isEmpty()) {
            etPUEmail.error = "Email is required"
            etPUEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            etPUPassword.error = "Password is required"
            etPUPassword.requestFocus()
            return
        }

        val customer = RegistrationModel(cusId,name, nic, address, phoneNumber, email, password)
        dbRef.setValue(customer).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Error updating profile: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }

        val intent = Intent(this, Profile::class.java)
        intent.putExtra("cusId", cusId)
//        intent.putExtra("password", etPUPassword.text.toString())
        startActivity(intent)

        finish()
    }
}