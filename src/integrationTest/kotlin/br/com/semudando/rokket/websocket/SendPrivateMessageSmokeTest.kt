package br.com.semudando.rokket.websocket

import br.com.semudando.rokket.BotConfiguration
import br.com.semudando.rokket.RocketChatListener
import io.kotest.core.spec.style.FunSpec
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.LogLevel.ALL
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.coroutines.delay
import java.math.BigInteger
import java.security.MessageDigest

class SendPrivateMessageSmokeTest : FunSpec({

  val rocketListener = listener(RocketChatListener())

  val configuration by lazy { BotConfiguration(rocketListener.host, rocketListener.port, rocketListener.adminUsername, rocketListener.adminPassword.sha256()) }
  val target by lazy { RocketClient(WebSocketClient(HttpClient { install(WebSockets); Logging { level = ALL } }, configuration), configuration) }


  test("Create User") {
    target.registerUser("a@b.c", "pass", "name")
    delay(100000)
  }

})

private fun String.sha256(): String {
  val md = MessageDigest.getInstance("SHA-256")
  return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}
