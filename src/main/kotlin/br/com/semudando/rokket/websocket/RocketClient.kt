package br.com.semudando.rokket.websocket

import br.com.semudando.rokket.BotConfiguration
import br.com.semudando.rokket.websocket.message.outgoing.ConnectMessage
import br.com.semudando.rokket.websocket.message.outgoing.LoginMessage
import br.com.semudando.rokket.websocket.message.outgoing.PasswordData
import br.com.semudando.rokket.websocket.message.outgoing.PongMessage
import br.com.semudando.rokket.websocket.message.outgoing.UserData
import br.com.semudando.rokket.websocket.message.outgoing.WebserviceRequestParam
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

internal class RocketClient(
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
    webSocketClient.sendMessage(LoginMessage(createLoginParam()))
  }

  private fun createLoginParam() = WebserviceRequestParam(
    UserData(botConfiguration.username),
    PasswordData(botConfiguration.sha256Password)
  )

  private fun preparePong() {
    launch {
      //FIXME while true with a fixed 1 second delay
      while (true) {
        webSocketClient.sendMessage(PongMessage())
        delay(1000)
      }
    }
  }

}
