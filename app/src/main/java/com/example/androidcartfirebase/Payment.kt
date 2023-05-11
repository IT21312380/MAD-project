package com.example.androidcartfirebase

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Payment : AppCompatActivity() {

    private lateinit var etCardNumber: EditText
    private lateinit var etExpiryDate: EditText
    private lateinit var etCvc: EditText
    private lateinit var etAmount: EditText
    private lateinit var etBtnCal: Button
    private lateinit var etTotalAmount: EditText
    private lateinit var etdDate: TextView
    private lateinit var etdDateInput: EditText
    private lateinit var etBtnPay: Button

    private lateinit var dbRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        etCardNumber = findViewById(R.id.etCardNumber)
        etExpiryDate = findViewById(R.id.etExpiryDate)
        etCvc = findViewById(R.id.etCvc)
        etAmount = findViewById(R.id.etAmount)
        etTotalAmount = findViewById(R.id.etTotalAmount)
        etdDate = findViewById(R.id.etdDate)
        etdDateInput = findViewById(R.id.etdDateInput)
        etBtnPay = findViewById(R.id.etBtnPay)
        etBtnCal = findViewById(R.id.etBtnCal)

        dbRef = FirebaseDatabase.getInstance().getReference("Payments")

        etBtnCal.setOnClickListener{
            calculateTotalAmount()
        }

        etBtnPay.setOnClickListener{
            val paymentId = dbRef.push().key!!

            savePaymentData(paymentId)


        }

    }

    private fun calculateTotalAmount() {
        val amount = etAmount.text.toString().toDoubleOrNull()

        if (amount != null) {
            val totalAmount = amount + (amount * 0.1)
            etTotalAmount.setText(totalAmount.toString())
        } else {
            etTotalAmount.setText("")
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePaymentData(paymentId: String) {
        val cardNum = etCardNumber.text.toString()
        val cardExpDate = etExpiryDate.text.toString()
        val cvc = etCvc.text.toString()
        val amount = etAmount.text.toString()
        val totalAmount = etTotalAmount.text.toString()
        val deliveryDate = etdDateInput.text.toString()

//        if (etCardNumber.isEmpty(cardNum)) {
//            etCardNumber.setError("Username is required");
//            return;
//        }

        if (cardNum.isEmpty()){
            etCardNumber.error = "Card Number is required"
            return;
        }
        else if  (cardExpDate.isEmpty()){
            etExpiryDate.error = "Please enter Expire Date of Your Card"
            return;
        }
        else if (cvc.isEmpty()){
            etCvc.error = "Please enter CVC"
            return;
        }
        else if (amount.isEmpty()){
            etAmount.error = "Please enter Amount"
            return;
        }
        else if (totalAmount.isEmpty()){
            etTotalAmount.error = "Please calculate total amount"
            return;
        }
        else if (deliveryDate.isEmpty()){
            etdDateInput.error = "Please enter Delivery Date"
            return;
        }

        else if (etCardNumber.length() < 12) {
            etCardNumber.error = "Enter a valid card number"
            return;
        }

        else if (etCvc.length() < 3) {
            etCvc.error = "Enter a valid CVC"
            return;
        }
        else{
            val intent = Intent(this, Bill::class.java)
            intent.putExtra("paymentId", paymentId)
            startActivity(intent)
        }

//

        val payment = PaymentModel(
            paymentId,
//            cardNum,
//            cardExpDate,
//            cvc,
            amount,
            totalAmount,
            deliveryDate
        )

        dbRef.child(paymentId).setValue(payment)
            .addOnCompleteListener {
                Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_LONG).show()

                etCardNumber.text.clear()
                etExpiryDate.text.clear()
                etCvc.text.clear()
                etAmount.text.clear()
                etTotalAmount.text.clear()
                etdDateInput.text.clear()

            }.addOnFailureListener {err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }

}