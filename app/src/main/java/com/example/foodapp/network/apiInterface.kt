package com.example.foodapp.network

import FoodModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("api/json/v1/1/random.php")
    fun getData(): Call<FoodModel>
}
