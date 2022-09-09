package org.linphone.model.request

data class LoginRequest(
    val username : String,
    val password : String,
    val domain : String
)