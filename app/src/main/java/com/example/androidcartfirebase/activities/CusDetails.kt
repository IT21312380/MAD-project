package com.example.androidcartfirebase.activities



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.androidcartfirebase.R
import com.example.androidcartfirebase.models.CustomerComModel
import com.google.firebase.database.FirebaseDatabase

class CusDetails : AppCompatActivity() {
    private lateinit var tvCusId: TextView
    private lateinit var tvCusName: TextView
    private lateinit var tvCusComment: TextView
    private lateinit var tvRt: RatingBar
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cus_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("cusId").toString(),
                intent.getStringExtra("cusName").toString(),
                intent.getFloatExtra("rating",0f)

            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("cusId").toString()
            )
        }

    }

    private fun initView() {
        tvCusId = findViewById(R.id.tvCusId)
        tvCusName = findViewById(R.id.tvCusName)
        tvCusComment = findViewById(R.id.tvCusComment)
        tvRt = findViewById(R.id.tvRt)



        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvCusId.text = intent.getStringExtra("cusId")
        tvCusName.text = intent.getStringExtra("cusName")
        tvCusComment.text = intent.getStringExtra("cusComment")
        tvRt.rating = intent.getFloatExtra("rating", 0f)

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("CustomerCom").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Customer data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(
        cusId: String,
        cusName: String,
        rating: Float
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etCusName = mDialogView.findViewById<EditText>(R.id.etCusName)
        val etCusComment = mDialogView.findViewById<EditText>(R.id.etCusComment)
        val etRt = mDialogView.findViewById<RatingBar>(R.id.etRt)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etCusName.setText(intent.getStringExtra("cusName").toString())
        etCusComment.setText(intent.getStringExtra("cusComment").toString())
        etRt.rating = rating

        mDialog.setTitle("Updating $cusName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateCusData(
                cusId,
                etCusName.text.toString(),
                etCusComment.text.toString(),
                etRt.rating
            )

            Toast.makeText(applicationContext, "Customer Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvCusName.text = etCusName.text.toString()
            tvCusComment.text = etCusComment.text.toString()
            tvRt.rating = etRt.rating

            alertDialog.dismiss()
        }
    }

    private fun updateCusData(
        id: String,
        name: String,
        comment: String,
        rating: Float
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("CustomerCom").child(id)
        val cusInfo = CustomerComModel(id, name, comment, rating)
        dbRef.setValue(cusInfo)
    }
}
