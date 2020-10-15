/*
 * Copyright (c) 2010-2020 Belledonne Communications SARL.
 *
 * This file is part of linphone-android
 * (see https://www.linphone.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.linphone.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager.*
import androidx.core.content.ContextCompat.getSystemService
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import org.linphone.LinphoneApplication.Companion.coreContext
import org.linphone.LinphoneApplication.Companion.corePreferences
import org.linphone.R
import org.linphone.core.*
import org.linphone.core.tools.Log

/**
 * Various utility methods for Linphone SDK
 */
class LinphoneUtils {
    companion object {
        private const val RECORDING_DATE_PATTERN = "dd-MM-yyyy-HH-mm-ss"

        fun getDisplayName(address: Address): String {
            return address.displayName ?: address.username ?: ""
        }

        fun getDisplayableAddress(address: Address?): String {
            if (address == null) return "[null]"
            return if (corePreferences.replaceSipUriByUsername) {
                address.username ?: address.asStringUriOnly()
            } else {
                address.asStringUriOnly()
            }
        }

        fun isLimeAvailable(): Boolean {
            val core = coreContext.core
            return core.limeX3DhAvailable() && core.limeX3DhEnabled() &&
                    core.limeX3DhServerUrl != null &&
                    core.defaultProxyConfig?.conferenceFactoryUri != null
        }

        fun isGroupChatAvailable(): Boolean {
            val core = coreContext.core
            return core.defaultProxyConfig?.conferenceFactoryUri != null
        }

        fun createOneToOneChatRoom(participant: Address, isSecured: Boolean = false): ChatRoom? {
            val core: Core = coreContext.core
            val defaultProxyConfig = core.defaultProxyConfig

            val params = core.createDefaultChatRoomParams()
            params.enableGroup(false)
            params.backend = ChatRoomBackend.Basic
            if (isSecured) {
                params.subject = AppUtils.getString(R.string.chat_room_dummy_subject)
                params.enableEncryption(true)
                params.backend = ChatRoomBackend.FlexisipChat
            }

            val participants = arrayOf(participant)

            return core.searchChatRoom(params, defaultProxyConfig?.contact, null, participants)
                ?: core.createChatRoom(params, defaultProxyConfig?.contact, participants)
        }

        fun deleteFilesAttachedToEventLog(eventLog: EventLog) {
            if (eventLog.type == EventLog.Type.ConferenceChatMessage) {
                val message = eventLog.chatMessage
                if (message != null) deleteFilesAttachedToChatMessage(message)
            }
        }

        fun deleteFilesAttachedToChatMessage(chatMessage: ChatMessage) {
            for (content in chatMessage.contents) {
                val filePath = content.filePath
                if (filePath != null && filePath.isNotEmpty()) {
                    Log.i("[Linphone Utils] Deleting file $filePath")
                    FileUtils.deleteFile(filePath)
                }
            }
        }

        fun getRecordingFilePathForAddress(address: Address): String {
            val displayName = getDisplayName(address)
            val dateFormat: DateFormat = SimpleDateFormat(
                RECORDING_DATE_PATTERN,
                Locale.getDefault()
            )
            val fileName = "${displayName}_${dateFormat.format(Date())}.mkv"
            return FileUtils.getFileStoragePath(fileName).absolutePath
        }

        fun getRecordingDateFromFileName(name: String): Date {
            return SimpleDateFormat(RECORDING_DATE_PATTERN, Locale.getDefault()).parse(name)
        }

        fun checkIfNetworkHasLowBandwidth(context: Context): Boolean {
            val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                    return when (networkInfo.subtype) {
                        NETWORK_TYPE_EDGE, NETWORK_TYPE_GPRS, NETWORK_TYPE_IDEN -> true
                        else -> false
                    }
                }
            }
            // In doubt return false
            return false
        }
    }
}