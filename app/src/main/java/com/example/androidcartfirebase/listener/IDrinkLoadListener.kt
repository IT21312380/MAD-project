package com.example.androidcartfirebase.listener

import com.example.androidcartfirebase.model.DrinkModel

interface IDrinkLoadListener {
    fun onDrinkLoadSuccess(drinkModelList:List<DrinkModel>?)
    fun onDrinkLoadFailed(message:String?)
}