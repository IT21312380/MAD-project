package com.example.androidcartfirebase

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.database.*

class Bill : AppCompatActivity() {

    private lateinit var etBBtnOk: Button
    private lateinit var dbRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)
        val paymentId = intent.getStringExtra("paymentId") ?: ""
        if (paymentId.isBlank()) {
            Toast.makeText(this, "Payment ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        dbRef = FirebaseDatabase.getInstance().getReference("Payments").child(paymentId)

        val etBGetPaymentID: TextView = findViewById(R.id.etBGetPaymentID)
//        val etBGetCardNumber: EditText = findViewById(R.id.etBGetCardNumber)
        val etBGetAmount: TextView = findViewById(R.id.etBGetAmount)
        val etBGetTotAmount: TextView= findViewById(R.id.etBGetTotAmount)
        val etBGetDDate: TextView = findViewById(R.id.etBGetDDate)
        etBBtnOk = findViewById(R.id.etBBtnOk)

        etBBtnOk.setOnClickListener {
            val intent = Intent(this@Bill, Payment::class.java)
            startActivity(intent)
        }

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val payment = snapshot.getValue(PaymentModel::class.java)
                    if (payment != null) {
                        etBGetPaymentID.setText(payment.paymentId)
//                        etBGetCardNumber.setText(payment.cardNum)
                        etBGetAmount.setText(payment.amount)
                        etBGetTotAmount.setText(payment.totalAmount)
                        etBGetDDate.setText(payment.deliveryDate)
                    } else {
                        Toast.makeText(this@Bill, "Payment data not found", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@Bill, "Payment ID not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Bill, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e("Bill", "Firebase error", error.toException())
                finish()
            }
        })
    }
}