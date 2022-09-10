package org.linphone.model.request

data class PushNotificationRequest(
    val deviceToken: String,
    val callerId: String,
    val domain: String,
    val extension: String
)
