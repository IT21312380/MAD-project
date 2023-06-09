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

class Registration : AppCompatActivity() {

    private lateinit var etRName: EditText
    private lateinit var etRNIC: EditText
    private lateinit var etRAddress: EditText
    private lateinit var etRPhoneNumber: EditText
    private lateinit var etREmail: EditText
    private lateinit var etRPassword: EditText
    private lateinit var etRBtnSubmit: Button
    private lateinit var etRLogin: Button

    private lateinit var dbRef: DatabaseReference



    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        etRName = findViewById(R.id.etRName)
        etRNIC = findViewById(R.id.etRNIC)
        etRAddress = findViewById(R.id.etRAddress)
        etRPhoneNumber = findViewById(R.id.etRPhoneNumber)
        etREmail = findViewById(R.id.etREmail)
        etRPassword = findViewById(R.id.etRPassword)
        etRBtnSubmit = findViewById(R.id.etRBtnSubmit)
        etRLogin = findViewById(R.id.etRLogin)


        dbRef = FirebaseDatabase.getInstance().getReference("Customers")

        etRLogin.setOnClickListener {
            val intent = Intent(this@Registration, Login::class.java)
            startActivity(intent)
        }

        etRBtnSubmit.setOnClickListener {
            val cusId = dbRef.push().key!!
            saveRegistrationData(cusId)


        }



    }

    private fun saveRegistrationData(cusId: String) {
        val name = etRName.text.toString()
        val nic = etRNIC.text.toString()
        val address = etRAddress.text.toString()
        val phoneNumber = etRPhoneNumber.text.toString()
        val email = etREmail.text.toString()
        val password = etRPassword.text.toString()
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (name.isEmpty()){
            etRName.error = "Please enter Your Name"
            return;
        }
        else if (nic.isEmpty()){
            etRNIC.error = "Please enter your NIC"
            return;
        }
        else if (address.isEmpty()){
            etRAddress.error = "Please enter your Address"
            return;
        }
        else if (phoneNumber.isEmpty()){
            etRPhoneNumber.error = "Please enter your Phone Number"
            return;
        }

        else if (email.isEmpty()) {
            etREmail.error = "Please enter your email"
            return;
        } else if (!email.matches(emailPattern.toRegex())) {
            etREmail.error = "Please enter a valid email address"
            return;
        }
        else if (password.isEmpty()){
            etRPassword.error = "Please enter your Password"
            return;
        }

        else if (etRNIC.length() < 12) {
            etRNIC.error = "Enter a valid NIC"
            return;
        }

        else if (etRPhoneNumber.length() < 10) {
            etRPhoneNumber.error = "Enter a valid phone number"
            return;
        }
        else{
            val intent = Intent(this, Login::class.java)
            intent.putExtra("cusId", cusId)
            startActivity(intent)
            }


        val customer = RegistrationModel(
            cusId,
            name,
            nic,
            address,
            phoneNumber,
            email,
            password
        )

        dbRef.child(cusId).setValue(customer)
            .addOnCompleteListener {
                Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_LONG).show()

                etRName.text.clear()
                etRNIC.text.clear()
                etRAddress.text.clear()
                etRPhoneNumber.text.clear()
                etREmail.text.clear()
                etRPassword.text.clear()

            }.addOnFailureListener {err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }

}