package com.example.foodapp.network

import FoodModel
import Meal
import android.content.Context
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    fun getData(context: Context, callback: (Meal?) -> Unit) {

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ApiInterface = retrofit.create(ApiInterface::class.java)
        val call: Call<FoodModel> = service.getData()

        call.enqueue(object : Callback<FoodModel> {
            override fun onResponse(call: Call<FoodModel>, response: Response<FoodModel>) {
                if (response.isSuccessful) {
                    val data: FoodModel? = response.body()
                    if (data != null && data.meals.isNotEmpty()) {
                        val meal = data.meals[0] // Get the first meal from the list
                        Log.d("ApiClient", "Success: ${response.message()}")
                        Log.d("ApiClient", "Data: $meal")
                        callback(meal)
                    } else {
                        Log.e("ApiClient", "No meals found or response body is null")
                        callback(null)
                    }
                } else {
                    Log.e("ApiClient", "Response not successful: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FoodModel>, t: Throwable) {
                Log.e("ApiClient", "API call failed", t)
            }
        })
    }
}
