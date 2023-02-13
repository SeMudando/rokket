package br.com.semudando.rokket

import br.com.semudando.rokket.handler.message.AbstractMessageHandler
import br.com.semudando.rokket.util.ReconnectWaitService
import br.com.semudando.rokket.websocket.ConnectMessage
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.wss
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.reflections.Reflections


public class Bot(
  private val botConfiguration: BotConfiguration,
  private val eventHandler: EventHandler,
) {
  private val httpClient = HttpClient(CIO) { install(WebSockets) }

  internal companion object {
    val subscriptionService = SubscriptionService()
    var userId: String? = null
    var authToken: String? = null
  }

  public fun start() {
    runBlocking { runWebsocketClient() }

  }

  private suspend fun runWebsocketClient() {
    while (true) {
      httpClient.wss(HttpMethod.Get, botConfiguration.host, path = "/websocket") {
        val messageOutputRoutine = launch { receiveMessages() }
        val userInputRoutine = launch { sendMessage(ConnectMessage()) }

        userInputRoutine.join()
        messageOutputRoutine.join()
      }

      ReconnectWaitService.instance.wait()


      // we must clear the list of all channels
      // to ensure that upon reconnect, the bot
      // will properly re-subscribe to all channels
      subscriptionService.reset()
    }
  }

  private suspend fun DefaultClientWebSocketSession.sendMessage(message: Any) {
    // TODO implement token refresh

    val jsonMessage = ObjectMapper().writeValueAsString(message)
    send(Frame.Text(jsonMessage))
  }

  private suspend fun DefaultClientWebSocketSession.receiveMessages() {
    val handlers = Reflections(AbstractMessageHandler::class.java.packageName)
      .getSubTypesOf(AbstractMessageHandler::class.java)
      .map {
        it
          .getDeclaredConstructor(
            EventHandler::class.java,
            BotConfiguration::class.java
          )
          .newInstance(eventHandler, botConfiguration)
      }
      .associateBy { it.getHandledMessage() }

    for (message in incoming) {
      launch {
        if (message !is Frame.Text) {
          return@launch
        }
        val text = message.readText()

        val data = ObjectMapper().readTree(text)
        val messageType = data.get("msg")?.textValue() ?: return@launch
        if (messageType !in handlers) {
          return@launch
        }

        handlers[messageType]?.handleMessage(data)?.forEach { sendMessage(it) }
      }
    }
  }
}

