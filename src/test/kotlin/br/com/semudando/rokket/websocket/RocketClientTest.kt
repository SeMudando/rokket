package br.com.semudando.rokket.websocket

import br.com.semudando.rokket.BotConfiguration
import br.com.semudando.rokket.websocket.message.outgoing.connection.ConnectMessage
import br.com.semudando.rokket.websocket.message.outgoing.connection.Login
import br.com.semudando.rokket.websocket.message.outgoing.heartbeat.Pong
import io.kotest.core.spec.style.FunSpec
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.delay

class RocketClientTest : FunSpec({

  val socketClient = mockk<WebSocketClient>(relaxed = true)
  val configuration = BotConfiguration("host", "user", "pass")

  context("Initialization") {
    RocketClient(socketClient, configuration)

    test("Connect") {
      coVerify(exactly = 1) { socketClient.sendMessage(ConnectMessage()) }
    }

    test("Login") {
      coVerify(exactly = 1) {
        socketClient.sendMessage(Login("user", "pass"))
      }
    }

    test("Connect and login order") {
      coVerifyOrder {
        socketClient.sendMessage(ConnectMessage())
        socketClient.sendMessage(Login("user", "pass"))

      }
    }

    test("Pong heartbeat setup") {
      //FIXME Unit test with hardcoded delay
      delay(2500)
      coVerify(atLeast = 3) {
        socketClient.sendMessage(Pong())
      }
    }
  }
})
