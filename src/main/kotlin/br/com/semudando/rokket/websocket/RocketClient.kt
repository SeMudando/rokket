package br.com.semudando.rokket.websocket

import br.com.semudando.rokket.BotConfiguration
import br.com.semudando.rokket.websocket.message.outgoing.connection.ConnectMessage
import br.com.semudando.rokket.websocket.message.outgoing.connection.Login
import br.com.semudando.rokket.websocket.message.outgoing.heartbeat.Pong
import br.com.semudando.rokket.websocket.message.outgoing.user.RegisterUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

public class RocketClient(
  private val webSocketClient: WebSocketClient,
  private val botConfiguration: BotConfiguration
) : CoroutineScope by CoroutineScope(Dispatchers.IO) {

  init {
    runBlocking {
      connect()
      login()
      preparePong()
    }
  }

  private suspend fun connect() {
    webSocketClient.sendMessage(ConnectMessage())
  }

  private suspend fun login() {
    webSocketClient.sendMessage(Login(botConfiguration.username, botConfiguration.sha256Password))
  }

  public suspend fun registerUser(email: String, pass: String, name: String, secretUrl: String? = null) {
    webSocketClient.sendMessage(RegisterUser(email, pass, name, secretUrl)) {
      println("Woow! MEssage! $it")
    }
  }


  @Suppress("MagicNumber")
  //FIXME while true with a fixed 1 second delay
  private fun preparePong() {
    launch {
      while (true) {
        webSocketClient.sendMessage(Pong())
        delay(1000)
      }
    }
  }
}
