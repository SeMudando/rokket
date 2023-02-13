package br.com.semudando.rokket.handler.message

import br.com.semudando.rokket.BotConfiguration
import br.com.semudando.rokket.EventHandler
import com.fasterxml.jackson.databind.JsonNode

abstract class AbstractMessageHandler(val eventHandler: EventHandler, val botConfiguration: BotConfiguration) {
  abstract fun getHandledMessage(): String

  abstract fun handleMessage(data: JsonNode): Array<Any>
}
