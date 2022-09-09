package org.linphone.services

import com.google.gson.JsonObject
import org.linphone.model.response.login.LoginModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("/Login")
//    @POST("/LoginStatus")
//    @POST("/PushNotification")
    fun login(@Body body: JsonObject?): Call<LoginModel?>?
    //    @POST("/LoginStatus")
//    fun loginStatus(@Body body: JsonObject?) Call<>
}
