package com.example.androidcartfirebase.activities



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import com.example.androidcartfirebase.models.CustomerComModel
import com.example.androidcartfirebase.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etCusName: EditText
    private lateinit var etCusComment: EditText
    private lateinit var btnSaveData: Button
    private lateinit var etRt: RatingBar
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etCusName = findViewById(R.id.etCusName)
        etCusComment = findViewById(R.id.etCusComment)
        btnSaveData = findViewById(R.id.btnSave)
        etRt = findViewById(R.id.etRt)
        dbRef = FirebaseDatabase.getInstance().getReference("CustomerCom")

        btnSaveData.setOnClickListener {
            saveCustomerData()
        }
    }

    private fun saveCustomerData(){
        //getting values
        val cusName = etCusName.text.toString().trim()
        val cusComment = etCusComment.text.toString().trim()
        val rating = etRt.rating

        if (cusName.isEmpty()) {
            etCusName.error = "Please enter name"
            etCusName.requestFocus()
            return
        }

        if (cusComment.isEmpty()) {
            etCusComment.error = "Please enter comment"
            etCusComment.requestFocus()
            return
        }

        val cusId = dbRef.push().key ?: ""

        val customer = CustomerComModel(cusId, cusName, cusComment, rating)

        dbRef.child(cusId).setValue(customer)
            .addOnSuccessListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()
                etCusName.text.clear()
                etCusComment.text.clear()
                etRt.rating=0f
            }
            .addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}
