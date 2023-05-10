package com.example.androidcartfirebase

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class welcomelog : AppCompatActivity() {

    private lateinit var seller: Button
    private lateinit var buyer: Button



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcomelog)

        seller = findViewById(R.id.seller)
        buyer = findViewById(R.id. buyer)

        seller.setOnClickListener {
            val intent = Intent(this@welcomelog, Login::class.java)
            startActivity(intent)
        }

        buyer.setOnClickListener {
            val intent = Intent(this@welcomelog, BuyerLogin::class.java)
            startActivity(intent)
        }


    }
}