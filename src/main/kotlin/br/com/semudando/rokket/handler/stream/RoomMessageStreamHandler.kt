package br.com.semudando.rokket.handler.stream

import br.com.semudando.rokket.BotConfiguration
import br.com.semudando.rokket.EventHandler
import br.com.semudando.rokket.util.MessageHelper
import br.com.semudando.rokket.websocket.message.outgoing.SendMessageMessage
import com.fasterxml.jackson.databind.JsonNode

public class RoomMessageStreamHandler(
  eventHandler: EventHandler,
  botConfiguration: BotConfiguration,
) : AbstractStreamHandler(eventHandler, botConfiguration) {
  override fun getHandledStream(): String = "stream-room-messages"

  @Suppress("UNCHECKED_CAST")
  override fun handleStreamMessage(data: JsonNode): List<List<Any>> {
    val args = data.get("fields")?.get("args") ?: emptyList()

    return args
      .filter { !isIgnoredRoom(it) }
      .map { handleStreamMessageItem(it) }
  }

  private fun getRoomName(item: JsonNode): String? {
    val roomId = item.get("rid").textValue()

    return br.com.semudando.rokket.Bot.subscriptionService.getChannelNameById(roomId)
  }

  private fun isIgnoredRoom(item: JsonNode): Boolean {
    val roomName = getRoomName(item)
    if (roomName != null && botConfiguration.ignoredChannels.contains(roomName)) {
      return true
    }
    return false
  }

  private fun handleStreamMessageItem(messageNode: JsonNode): List<SendMessageMessage> {
    val messageText = messageNode.get("msg").textValue().trim()
    val roomId = messageNode.get("rid").textValue()
    val roomName = getRoomName(messageNode)
    val timestamp = messageNode.get("ts")?.get("\$date")?.asLong()

    if (isOldMessage(timestamp, roomId)) {
      return emptyList()
    }

    val i = messageNode.get("bot")?.get("i")?.textValue() ?: ""
    val botMessage = i.isNotBlank()
    if (botMessage) {
    }

    val username = messageNode.get("u")?.get("username")?.textValue() ?: ""
    val userId = messageNode.get("u")?.get("_id")?.textValue() ?: ""

    val channelType =
      br.com.semudando.rokket.Bot.subscriptionService.getRoomType(roomId) ?: EventHandler.ChannelType.OTHER
    val channel = EventHandler.Channel(roomId, roomName, channelType)
    val user = EventHandler.User(userId, username)
    val message = EventHandler.Message(messageText, botMessage)

    val outgoingMessages = if (username == botConfiguration.username) {
      eventHandler.handleOwnMessage(channel, user, message)
    } else {
      eventHandler.handleRoomMessage(channel, user, message)
    }
    return outgoingMessages.map {
      MessageHelper.instance.createSendMessage(roomId, it.message, botConfiguration.botId, it.emoji, it.username)
    }
  }

  private fun isOldMessage(timestamp: Long?, roomId: String): Boolean {
    if (timestamp != null) {
      // There may be multiple Websocket events for the same message in a very short timeframe
      // e.g. in case some other bot automatically adds multiple reactions to the most recent message.
      // To avoid the message being processed multiple times, we need to synchronize here.
      synchronized(this) {
        val newestTimestampSeen = br.com.semudando.rokket.Bot.subscriptionService.getNewestTimestampSeen(roomId)
        if (newestTimestampSeen != null && timestamp <= newestTimestampSeen) {
          return true
        }
        br.com.semudando.rokket.Bot.subscriptionService.updateNewestTimestampSeen(roomId, timestamp)
      }
    }

    return false
  }

  private fun getTimestamp(jsonNode: JsonNode): Long {
    val dateNode = jsonNode.get("fields")?.get("args")?.get(0)?.get("ts")?.get("\$date") ?: return 0L
    if (dateNode.isLong) {
      return dateNode.asLong()
    }
    return 0L
  }
}
