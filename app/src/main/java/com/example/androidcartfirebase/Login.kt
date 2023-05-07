package com.example.androidcartfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {

    private lateinit var etLEmail: EditText
    private lateinit var etLPassword: EditText
    private lateinit var etLBtnLogin: Button
    private lateinit var etLBtnSignup: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etLEmail = findViewById(R.id.etLEmail)
        etLPassword = findViewById(R.id.etLPassword)
        etLBtnLogin = findViewById(R.id.etLBtnLogin)
        etLBtnSignup = findViewById(R.id.etLBtnSignup)

        dbRef = FirebaseDatabase.getInstance().getReference("Customers")

        etLBtnSignup.setOnClickListener {
            val intent = Intent(this@Login, Registration::class.java)
            startActivity(intent)
        }

        etLBtnLogin.setOnClickListener {
            val email = etLEmail.text.toString()
            val password = etLPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter Email and Password", Toast.LENGTH_LONG).show()
            } else {
                dbRef.orderByChild("email").equalTo(email)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (userSnapshot in dataSnapshot.children) {
                                    val user = userSnapshot.getValue(RegistrationModel::class.java)
                                    if (user?.password == password) {
                                        val intent = Intent(this@Login,MainActivity::class.java)
                                        intent.putExtra("cusId", user?.cusId)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@Login,
                                            "Invalid Password",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    this@Login,
                                    "User not found",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Toast.makeText(
                                this@Login,
                                "Error ${databaseError.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            }
        }
    }
}