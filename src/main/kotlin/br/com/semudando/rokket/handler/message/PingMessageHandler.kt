package br.com.semudando.rokket.handler.message

import br.com.semudando.rokket.BotConfiguration
import br.com.semudando.rokket.EventHandler
import br.com.semudando.rokket.websocket.PongMessage
import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDateTime

class PingMessageHandler(eventHandler: EventHandler, botConfiguration: BotConfiguration) :
  AbstractMessageHandler(eventHandler, botConfiguration) {
  companion object {
    var lastPing: LocalDateTime = LocalDateTime.now()

    fun updateLastPing() {
      lastPing = LocalDateTime.now()
    }
  }

  override fun getHandledMessage() = "ping"

  override fun handleMessage(data: JsonNode): Array<Any> {
    updateLastPing()
    return arrayOf(PongMessage())
  }
}
