package org.linphone.model.response.loginStatus

data class LoginStatusModel(
    val isAccessDenied: Boolean,
    val isSucceed: Boolean,
    val messages: List<MessageX>,
    val result: Boolean
)
