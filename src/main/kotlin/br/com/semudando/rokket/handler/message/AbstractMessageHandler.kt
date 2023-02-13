package br.com.semudando.rokket.handler.message

import br.com.semudando.rokket.BotConfiguration
import br.com.semudando.rokket.EventHandler
import com.fasterxml.jackson.databind.JsonNode

public abstract class AbstractMessageHandler(
  protected val eventHandler: EventHandler,
  protected val botConfiguration: BotConfiguration
) {
  public abstract fun getHandledMessage(): String

  public abstract fun handleMessage(data: JsonNode): Array<Any>
}
