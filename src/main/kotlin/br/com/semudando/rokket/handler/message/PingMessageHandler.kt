package br.com.semudando.rokket.handler.message

import br.com.semudando.rokket.BotConfiguration
import br.com.semudando.rokket.EventHandler
import br.com.semudando.rokket.websocket.message.outgoing.PongMessage
import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDateTime

public class PingMessageHandler(
  eventHandler: EventHandler,
  botConfiguration: BotConfiguration
) : AbstractMessageHandler(eventHandler, botConfiguration) {

  public companion object {
    private var lastPing: LocalDateTime = LocalDateTime.now()

    public fun updateLastPing() {
      lastPing = LocalDateTime.now()
    }
  }

  override fun getHandledMessage(): String = "ping"

  override fun handleMessage(data: JsonNode): Array<Any> {
    updateLastPing()
    return arrayOf(PongMessage())
  }
}
