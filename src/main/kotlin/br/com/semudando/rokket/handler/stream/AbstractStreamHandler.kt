package br.com.semudando.rokket.handler.stream

import br.com.semudando.rokket.BotConfiguration
import br.com.semudando.rokket.EventHandler
import com.fasterxml.jackson.databind.JsonNode

abstract class AbstractStreamHandler(val eventHandler: EventHandler, val botConfiguration: BotConfiguration) {
  abstract fun getHandledStream(): String

  abstract fun handleStreamMessage(data: JsonNode): List<List<Any>>
}
