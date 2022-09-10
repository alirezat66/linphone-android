package org.linphone.model.request

data class LoginStatusRequest(
    val deviceIp: String,
    val softwareVersion: String,
    val domain: String,
    val extension: String,
    val deviceToken: String,
    val deviceType: Int

)
// deviceType : 0 = android  , 1= ios
