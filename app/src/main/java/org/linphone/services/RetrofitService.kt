package org.linphone.services

import com.google.gson.JsonObject
import org.linphone.model.response.login.LoginModel
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitService {

    private const val BASE_URL: String = "https://pbx7.voipmax.nl/api/Device/"
    private const val APPLICATION_TOKEN: String = "SJKSHcnMdD8eMgFq090etbjrP8c"

    private val loginApiService: LoginApiService
    private val pushNotificationApiService: PushNotificationApiService
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        loginApiService = retrofit.create(LoginApiService::class.java)
        pushNotificationApiService = retrofit.create(PushNotificationApiService::class.java)
    }

    fun login(userName: String , password : String , domain: String,callback: LoginCallback) {
        val jsonObject  = JsonObject()
        jsonObject.addProperty("username","$userName@$domain")
        jsonObject.addProperty("password",password)
        loginApiService.login(jsonObject)?.enqueue(object : Callback<LoginModel?> {
            override fun onResponse(call: Call<LoginModel?>, response: Response<LoginModel?>) {
                response.body()?.let { callback.onSuccess(it) }
            }

            override fun onFailure(call: Call<LoginModel?>, t: Throwable) {
                callback.onError(Exception(t))
            }

        })
    }
    interface  LoginCallback{
        fun onSuccess(loginModel: LoginModel)
        fun onError(exception: Exception)
    }
}
