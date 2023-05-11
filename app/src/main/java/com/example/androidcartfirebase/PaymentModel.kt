package com.example.androidcartfirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference

data class PaymentModel(
    var paymentId: String? = "",
//    var cardNum: String? = "",
//    var cardExpDate: String? = "",
//    var cvc: String? = "",
    var amount: String? = "",
    var totalAmount: String? = "",
    var deliveryDate: String? = ""
){

    companion object {
        const val PAYMENTS_TABLE = "Payments"
    }

    fun calculateTotalAmount(): Double {
        val originalAmount = amount?.toDoubleOrNull() ?: 0.0
        val additionalAmount = originalAmount * 0.1 // 5% additional amount
        return originalAmount + additionalAmount
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "paymentId" to paymentId,
//            "cardNum" to cardNum,
//            "cardExpDate" to cardExpDate,
//            "cvc" to cvc,
            "amount" to amount,
            "totalAmount" to totalAmount,
            "deliveryDate" to deliveryDate
        )
    }

    fun saveToFirebase(databaseRef: DatabaseReference) {
        databaseRef.child(PAYMENTS_TABLE).child(paymentId ?: "").setValue(toMap())
    }

//    fun updateFromFirebase(dataSnapshot: DataSnapshot) {
//        dataSnapshot.getValue(PaymentModel::class.java)?.let { payment ->
//            paymentId = payment.paymentId
//            cardNum = payment.cardNum
//            cardExpDate = payment.cardExpDate
//            cvc = payment.cvc
//            amount = payment.amount
//            totalAmount = payment.totalAmount
//            deliveryDate = payment.deliveryDate
//        }
//    }
}