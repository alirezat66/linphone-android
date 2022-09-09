package org.linphone.model

import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


data class LoginModel(
    val isAccessDenied: Boolean,
    val isSucceed: Boolean,
    val messages: List<Message>,
    val result: Result
)

