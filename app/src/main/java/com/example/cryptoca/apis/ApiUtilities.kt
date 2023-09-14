package com.example.cryptoca.apis

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtilities {

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pro-api.coinmarketcap.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}