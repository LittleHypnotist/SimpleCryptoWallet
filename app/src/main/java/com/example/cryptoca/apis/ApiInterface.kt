package com.example.cryptoca.apis

import com.example.cryptoca.CriptoBD.MercadoCripto
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("v1/cryptocurrency/listings/latest?CMC_PRO_API_KEY=69256dde-22c2-4453-91e1-6f02ffe80e09&start=1&limit=100&convert=EUR")
    suspend fun getCriptoMercado(): Response<MercadoCripto>


}