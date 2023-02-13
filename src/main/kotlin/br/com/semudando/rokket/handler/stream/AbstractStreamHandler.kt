package br.com.semudando.rokket.handler.stream

import br.com.semudando.rokket.BotConfiguration
import br.com.semudando.rokket.EventHandler
import com.fasterxml.jackson.databind.JsonNode

public abstract class AbstractStreamHandler(
  public val eventHandler: EventHandler,
  public val botConfiguration: BotConfiguration,
) {
  public abstract fun getHandledStream(): String

  public abstract fun handleStreamMessage(data: JsonNode): List<List<Any>>
}
