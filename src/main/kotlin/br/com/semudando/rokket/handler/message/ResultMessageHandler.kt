package br.com.semudando.rokket.handler.message

import br.com.semudando.rokket.BotConfiguration
import br.com.semudando.rokket.EventHandler
import br.com.semudando.rokket.exception.LoginException
import br.com.semudando.rokket.util.MessageHelper
import br.com.semudando.rokket.util.RestApiClient
import br.com.semudando.rokket.websocket.RoomsGetMessage
import br.com.semudando.rokket.websocket.SubscribeMessage
import com.fasterxml.jackson.databind.JsonNode
import java.util.*

@Suppress("unused")
class ResultMessageHandler(eventHandler: EventHandler, botConfiguration: BotConfiguration) :
  AbstractMessageHandler(eventHandler, botConfiguration) {
  override fun getHandledMessage() = "result"

  override fun handleMessage(data: JsonNode) = when (val id = data.get("id")?.textValue()) {
    "login-initial" -> handleLoginInitial(data)
    "get-rooms-initial" -> handleGetRoomsResult(data)
    else -> {
      emptyArray()
    }
  }

  private fun handleLoginInitial(data: JsonNode): Array<Any> {
    if (data.has("error")) {
      throw LoginException(data.get("error")?.get("message")?.textValue() ?: "Unknown error")
    }
    val userId = data.get("result").get("id").textValue()

    br.com.semudando.rokket.Bot.userId = userId
    br.com.semudando.rokket.Bot.authToken = data.get("result").get("token").textValue()

    return arrayOf(
      RoomsGetMessage(id = "get-rooms-initial"),
      SubscribeMessage(
        id = "subscribe-stream-notify-user-rooms",
        name = "stream-notify-user",
        params = arrayOf("$userId/rooms-changed", false)
      ),
      SubscribeMessage(
        id = "subscribe-stream-notify-user-subscriptions",
        name = "stream-notify-user",
        params = arrayOf("$userId/subscriptions-changed", false)
      )
    )
  }

  private fun handleGetRoomsResult(data: JsonNode): Array<Any> {
    val rooms = data.get("result")
    val messages = rooms
      .filter { MessageHelper.instance.mapChannelType(it.get("t").textValue()) != EventHandler.ChannelType.OTHER }
      .mapNotNull {
        val id = it.get("_id").textValue()
        val name = it.get("name")?.textValue()
        val type = MessageHelper.instance.mapChannelType(it.get("t").textValue())

        br.com.semudando.rokket.Bot.subscriptionService.handleSubscription(id, name, type)
      }

    RestApiClient(botConfiguration.host).updateStatus()

    eventHandler.botInitialized()

    return messages.toTypedArray()
  }
}
