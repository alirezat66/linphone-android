package org.linphone.services

import com.google.gson.JsonObject
import org.linphone.model.LoginModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("")
    fun createUser(@Body body: JsonObject?): Call<LoginModel?>?
}