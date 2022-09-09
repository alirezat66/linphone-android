package org.linphone.model.response.login

data class Result(
    val audioCodec: String,
    val displayName: String,
    val domain: String,
    val iceAddress: String,
    val icePort: String,
    val iceSupport: Boolean,
    val mediaEncryption: String,
    val outboundProxyAddress: String,
    val outboundProxyEnabled: Boolean,
    val outboundProxyPort: String,
    val outboundProxyTransport: String,
    val password: String,
    val transport: String,
    val extension: String,
    val videoCodec: String,
    val videoEnabled: Boolean,
    val voicemailEnabled: Boolean,
    val voicemailPassword: String
)
