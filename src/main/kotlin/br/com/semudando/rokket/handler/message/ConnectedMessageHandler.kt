package br.com.semudando.rokket.handler.message

import br.com.semudando.rokket.BotConfiguration
import br.com.semudando.rokket.EventHandler
import br.com.semudando.rokket.util.ReconnectWaitService
import br.com.semudando.rokket.websocket.message.outgoing.LoginMessage
import br.com.semudando.rokket.websocket.message.outgoing.PasswordData
import br.com.semudando.rokket.websocket.message.outgoing.UserData
import br.com.semudando.rokket.websocket.message.outgoing.WebserviceRequestParam
import com.fasterxml.jackson.databind.JsonNode

public class ConnectedMessageHandler(
  eventHandler: EventHandler,
  botConfiguration: BotConfiguration
) : AbstractMessageHandler(eventHandler, botConfiguration) {
  override fun getHandledMessage(): String = "connected"

  override fun handleMessage(data: JsonNode): Array<Any> {
    val digest = botConfiguration.sha256Password

    ReconnectWaitService.instance.resetWaitingTime()
    PingMessageHandler.updateLastPing()

    return arrayOf(
      LoginMessage(
        id = "login-initial",
        params = listOf(
          WebserviceRequestParam(
            UserData(botConfiguration.username),
            PasswordData(digest, "sha-256")
          )
        )
      )
    )
  }
}
