package br.com.semudando.rokket.websocket

import br.com.semudando.rokket.BotConfiguration
import br.com.semudando.rokket.websocket.message.Message
import br.com.semudando.rokket.websocket.message.incoming.Response
import br.com.semudando.rokket.websocket.message.outgoing.Request
import br.com.semudando.rokket.websocket.message.toJson
import br.com.semudando.rokket.websocket.message.toMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

public fun interface CallbackHandler {
  public suspend fun process(message: Message)
}


//FIXME Untested class. I don't know how to test a websocket ¯\_(ツ)_/¯
public class WebSocketClient(
  private val httpClient: HttpClient,
  private val configuration: BotConfiguration,
) : CoroutineScope by CoroutineScope(IO) {

  private val callbacks: MutableMap<String, CallbackHandler> = mutableMapOf()

  private val session = runBlocking { httpClient.webSocketSession(configuration.apiUrl) }

  init {
    launch {
      while (true) {
        for (frame in session.incoming) {
          val message = frame.toMessage()
          if(message is Response) {
            receive(message)
          }
        }
      }
    }
  }


  public suspend fun sendMessage(message: Request, callback: CallbackHandler) {
    sendMessage(message)
    callbacks[message.id] = callback
  }

  private fun receive(message: Response?) {
    if(message == null) return

    launch {
      callbacks.remove(message.id)?.process(message)
    }
  }

  public suspend fun sendMessage(message: Message) {
    session.send(message.toJson())
  }
}
