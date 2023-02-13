package br.com.semudando.rokket.handler.message

import br.com.semudando.rokket.BotConfiguration
import br.com.semudando.rokket.EventHandler
import br.com.semudando.rokket.handler.stream.AbstractStreamHandler
import com.fasterxml.jackson.databind.JsonNode
import org.reflections.Reflections

@Suppress("unused")
class ChangedMessageHandler(eventHandler: EventHandler, botConfiguration: BotConfiguration) :
    AbstractMessageHandler(eventHandler, botConfiguration) {
    private val handlers: Map<String, AbstractStreamHandler> =
        Reflections(AbstractStreamHandler::class.java.packageName)
            .getSubTypesOf(AbstractStreamHandler::class.java)
            .map {
                it
                    .getDeclaredConstructor(EventHandler::class.java, BotConfiguration::class.java)
                    .newInstance(eventHandler, botConfiguration)
            }
            .associateBy { it.getHandledStream() }

    override fun getHandledMessage() = "changed"

    override fun handleMessage(data: JsonNode): Array<Any> {
        val collection = data.get("collection")?.textValue() ?: return emptyArray()

        return handlers[collection]!!
            .handleStreamMessage(data)
            .flatten()
            .toTypedArray()
    }
}
