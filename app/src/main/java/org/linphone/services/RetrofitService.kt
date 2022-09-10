package org.linphone.services

import com.google.gson.JsonObject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.linphone.model.request.LoginRequest
import org.linphone.model.request.LoginStatusRequest
import org.linphone.model.request.PushNotificationRequest
import org.linphone.model.response.login.LoginModel
import org.linphone.model.response.loginStatus.LoginStatusModel
import org.linphone.model.response.pushNotification.PushNotificationModel
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    private const val BASE_URL: String = "https://pbx7.voipmax.nl/api/Device/"
    private const val APPLICATION_TOKEN: String = "SJKSHcnMdD8eMgFq090etbjrP8c"

    private val loginApiService: LoginApiService
    private val pushNotificationApiService: PushNotificationApiService
    init {
        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(
                Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder.header("ApplicationToken", APPLICATION_TOKEN)
                    return@Interceptor chain.proceed(builder.build())
                }
            )
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
            .build()
        loginApiService = retrofit.create(LoginApiService::class.java)
        pushNotificationApiService = retrofit.create(PushNotificationApiService::class.java)
    }

    fun login(loginRequest: LoginRequest, callback: LoginCallback) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("username", "${loginRequest.username}@${loginRequest.domain}")
        jsonObject.addProperty("password", loginRequest.password)
        loginApiService.login(jsonObject)?.enqueue(object : Callback<LoginModel?> {
            override fun onResponse(call: Call<LoginModel?>, response: Response<LoginModel?>) {
                response.body()?.let { callback.onSuccess(it) }
            }

            override fun onFailure(call: Call<LoginModel?>, t: Throwable) {
                callback.onError(Exception(t))
            }
        })
    }

    fun loginStatus(
        loginStatusRequest: LoginStatusRequest,
        loginStatusCallback: LoginStatusCallback
    ) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("deviceIp", loginStatusRequest.deviceIp)
        jsonObject.addProperty("softwareVersion", loginStatusRequest.softwareVersion)
        jsonObject.addProperty("domain", loginStatusRequest.domain)
        jsonObject.addProperty("extension", loginStatusRequest.extension)
        jsonObject.addProperty("deviceToken", loginStatusRequest.deviceToken)
        jsonObject.addProperty("deviceType", loginStatusRequest.deviceType)

        loginApiService.loginStatus(jsonObject)?.enqueue(object : Callback<LoginStatusModel?> {
            override fun onResponse(
                call: Call<LoginStatusModel?>,
                response: Response<LoginStatusModel?>
            ) {
                response.body()?.let { loginStatusCallback.onSuccess(it) }
            }

            override fun onFailure(call: Call<LoginStatusModel?>, t: Throwable) {
                loginStatusCallback.onError(Exception(t))
            }
        })
    }

    fun pushNotification(
        pushNotificationRequest: PushNotificationRequest,
        pushNotificationCallback: PushNotificationCallback
    ) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("deviceToken", pushNotificationRequest.deviceToken)
        jsonObject.addProperty("callerId", pushNotificationRequest.callerId)
        jsonObject.addProperty("domain", pushNotificationRequest.domain)
        jsonObject.addProperty("extension", pushNotificationRequest.extension)
        pushNotificationApiService.pushNotificationRequest(jsonObject).enqueue(object :
                Callback<PushNotificationModel> {
                override fun onResponse(
                    call: Call<PushNotificationModel>,
                    response: Response<PushNotificationModel>
                ) {
                    response.body()?.let { pushNotificationCallback.onSuccess(it) }
                }

                override fun onFailure(call: Call<PushNotificationModel>, t: Throwable) {
                    pushNotificationCallback.onError(Exception(t))
                }
            })
    }
}

interface LoginCallback {
    fun onSuccess(loginModel: LoginModel)
    fun onError(exception: Exception)
}
interface LoginStatusCallback {
    fun onSuccess(loginStatusModel: LoginStatusModel)
    fun onError(exception: Exception)
}
interface PushNotificationCallback {
    fun onSuccess(pushNotificationModel: PushNotificationModel)
    fun onError(exception: Exception)
}
