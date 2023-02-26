package br.com.semudando.rokket.websocket

import br.com.semudando.rokket.BotConfiguration
import br.com.semudando.rokket.websocket.message.outgoing.ConnectMessage
import br.com.semudando.rokket.websocket.message.outgoing.LoginMessage
import br.com.semudando.rokket.websocket.message.outgoing.PasswordData
import br.com.semudando.rokket.websocket.message.outgoing.PongMessage
import br.com.semudando.rokket.websocket.message.outgoing.UserData
import br.com.semudando.rokket.websocket.message.outgoing.WebserviceRequestParam
import io.kotest.core.spec.style.FunSpec
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
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
        socketClient.sendMessage(
          LoginMessage(
            WebserviceRequestParam(UserData("user"), PasswordData("pass")),
          )
        )
      }
    }

    test("Connect and login order") {
      coVerifyOrder {
        socketClient.sendMessage(ConnectMessage())
        socketClient.sendMessage(
          LoginMessage(
            WebserviceRequestParam(UserData("user"), PasswordData("pass")),
          )
        )
      }
    }

    test("Pong heartbeat setup") {
      //FIXME Unit test with hardcoded delay
      delay(2500)
      coVerify(atLeast = 3) {
        socketClient.sendMessage(PongMessage())
      }
    }
  }
})
