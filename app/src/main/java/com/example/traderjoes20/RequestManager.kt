package com.example.traderjoes20

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.traderjoes20.Listeners.RandomRecipeResponseListener
import com.example.traderjoes20.RequestManager.CallRandomRecipes
import com.example.traderjoes20.Models.RandomRecipeApiResponse
import com.example.traderjoes20.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

class RequestManager(var context: Context) {
    private var retrofit = Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //call from main to get data
    fun getRandomRecipes(listener: RandomRecipeResponseListener) {
        val callRandomRecipes = retrofit.create(
            CallRandomRecipes::class.java
        )
        //number at end is the total number of random recipes 1-100
        val call = callRandomRecipes.callRandomRecipe(
            context.getString(R.string.api_key), "5"
        )
        call.enqueue(object : Callback<RandomRecipeApiResponse?> {
            override fun onResponse(
                call: Call<RandomRecipeApiResponse?>,
                response: Response<RandomRecipeApiResponse?>
            ) {
                if (!response.isSuccessful) {
                    listener.didError(response.message())
                    return
                }
                listener.didFetch(response.body(), response.message())
            }

            override fun onFailure(call: Call<RandomRecipeApiResponse?>, t: Throwable) {
                listener.didError(t.message)
            }
        })
    }

    private interface CallRandomRecipes {
        //from spoonacular GET RANDOM RECIPES
        @GET("recipes/random")
        fun callRandomRecipe(
            @Query("apiKey") apiKey: String?,
            @Query("number") number: String?
        ): Call<RandomRecipeApiResponse>
    }
}