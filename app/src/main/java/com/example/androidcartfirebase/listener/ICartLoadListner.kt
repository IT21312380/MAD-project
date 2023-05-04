package com.example.androidcartfirebase.listener

import com.example.androidcartfirebase.model.Cartmodel

interface ICartLoadListner {
fun onLoadCartSuccess(cartModelList: List<Cartmodel>)
fun onLoadCartFailed(message:String?)

}