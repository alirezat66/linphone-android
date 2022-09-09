package org.linphone.model.response.pushNotification

data class PushNotificationModel(
    val isAccessDenied: Boolean,
    val isSucceed: Boolean,
    val messages: List<Message>,
    val result: Boolean
)