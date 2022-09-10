package org.linphone.services

import com.google.gson.JsonObject
import org.linphone.model.response.pushNotification.PushNotificationModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PushNotificationApiService {
    @POST("/PushNotification")
    fun pushNotificationRequest(@Body body: JsonObject?): Call<PushNotificationModel>
}
