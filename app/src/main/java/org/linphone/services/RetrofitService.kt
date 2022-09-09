package org.linphone.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    private const val BASE_URL : String  = "https://pbx7.voipmax.nl/api/Device/"
    private const val APPLICATION_TOKEN : String  = "SJKSHcnMdD8eMgFq090etbjrP8c"
    val loginApiService : LoginApiService
    init {
        val retrofit= Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        loginApiService=retrofit.create(LoginApiService::class.java)
    }
    }
