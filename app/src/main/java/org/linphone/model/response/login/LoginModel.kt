package org.linphone.model.response.login

data class LoginModel(
    val isAccessDenied: Boolean,
    val isSucceed: Boolean,
    val messages: List<Message>,
    val result: Result
)
