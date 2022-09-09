package org.linphone.model.response.pushNotification

data class Message(
    val body: String,
    val title: String,
    val type: Int
)