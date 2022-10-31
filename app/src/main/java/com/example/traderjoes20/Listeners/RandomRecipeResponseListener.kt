package com.example.traderjoes20.Listeners

import com.example.traderjoes20.Models.RandomRecipeApiResponse

interface RandomRecipeResponseListener {
    fun didFetch(response: RandomRecipeApiResponse?, message: String?)
    fun didError(message: String?)
}