package at.rueckgr.kotlin.rocketbot.handler.stream

import at.rueckgr.kotlin.rocketbot.BotConfiguration
import at.rueckgr.kotlin.rocketbot.RoomMessageHandler
import at.rueckgr.kotlin.rocketbot.util.Logging
import at.rueckgr.kotlin.rocketbot.util.MessageHelper
import at.rueckgr.kotlin.rocketbot.util.logger
import at.rueckgr.kotlin.rocketbot.websocket.SendMessageMessage
import com.fasterxml.jackson.databind.JsonNode
import org.apache.commons.lang3.StringUtils
import kotlin.collections.ArrayList

@Suppress("unused")
class NotifyUserStreamHandler(roomMessageHandler: RoomMessageHandler, botConfiguration: BotConfiguration)
        : AbstractStreamHandler(roomMessageHandler, botConfiguration), Logging {
    override fun getHandledStream() = "stream-notify-user"

    override fun handleStreamMessage(data: JsonNode): List<List<Any>> {
        return when (MessageHelper.instance.getEventName(data)) {
            "rooms-changed" -> handleRoomsChangedEvent(data)
            else -> emptyList()
        }
    }

    private fun handleRoomsChangedEvent(data: JsonNode): List<List<Any>> {
        val args: JsonNode = data.get("fields")?.get("args") ?: return emptyList()

        return when (args.get(0).textValue()) {
            "updated" -> handleMessage(args)
            else -> emptyList()
        }
    }

    private fun handleMessage(args: JsonNode): List<List<Any>> {

        val items = ArrayList<JsonNode>()
        for (i in 1 until args.size()) {
            items.add(args.get(i))
        }

        return items
            .filter { !isIgnoredRoom(it) }
            .map { handleStreamMessageItem(it.get("lastMessage")) }
    }

    private fun isIgnoredRoom(item: JsonNode): Boolean {
        val roomName = item.get("fname")?.textValue() ?: return false // private messages don't have an fname
        if (botConfiguration.ignoredChannels.contains(roomName)) {
            logger().info("Message comes from ignored channel {}, ignoring", roomName)
            return true
        }
        return false
    }

    private fun handleStreamMessageItem(messageNode: JsonNode): List<Any> {
        val message = messageNode.get("msg").textValue()
        val roomId = messageNode.get("rid").textValue()

        val i = messageNode.get("bot")?.get("i")?.textValue() ?: ""
        val botMessage = StringUtils.isNotBlank(i)
        if (botMessage) {
            logger().debug("Message comes from self-declared bot")
        }

        val username = messageNode.get("u")?.get("username")?.textValue() ?: ""
        return handleUserMessage(roomId, username, message.trim(), botMessage)
    }

    private fun handleUserMessage(roomId: String, username: String, message: String, botMessage: Boolean): List<SendMessageMessage> {
        if (username == botConfiguration.username) {
            logger().debug("Message comes from myself, ignoring")
            return emptyList()
        }

        return roomMessageHandler
            .handle(username, message, botMessage)
            .map {
                MessageHelper.instance.createSendMessage(roomId, it.message, botConfiguration.botId, it.emoji, it.username)
            }
    }
}
