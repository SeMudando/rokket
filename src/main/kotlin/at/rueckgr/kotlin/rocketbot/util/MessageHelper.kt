package at.rueckgr.kotlin.rocketbot.util

import at.rueckgr.kotlin.rocketbot.EventHandler
import at.rueckgr.kotlin.rocketbot.websocket.SendMessageMessage
import java.util.UUID

class MessageHelper {
    companion object {
        val instance = MessageHelper()
    }

    fun createSendMessage(
        roomId: String,
        message: String,
        botId: String,
        emoji: String? = null,
        username: String? = null,
    ): SendMessageMessage {
        val id = UUID.randomUUID().toString()
        val botTag = mapOf("i" to botId)
        val params = mutableMapOf("_id" to id, "rid" to roomId, "msg" to message, "bot" to botTag)
        if (!emoji.isNullOrBlank()) {
            params["emoji"] = emoji
        }
        if (!username.isNullOrBlank()) {
            params["alias"] = username
        }
        return SendMessageMessage(id = id, params = listOf(params))
    }

    fun mapChannelType(t: String?) = when (t) {
        "c" -> EventHandler.ChannelType.CHANNEL
        "d" -> EventHandler.ChannelType.DIRECT
        else -> EventHandler.ChannelType.OTHER
    }
}
