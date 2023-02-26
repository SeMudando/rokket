package br.com.semudando.rokket.websocket.message

import br.com.semudando.rokket.websocket.message.incoming.heartbeat.Ping
import br.com.semudando.rokket.websocket.message.outgoing.connection.ConnectMessage
import br.com.semudando.rokket.websocket.message.outgoing.heartbeat.Pong
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class SerializerTest : FunSpec({

  test("Ping message") {
    """{
      "msg": "ping"
      }
    """.trimIndent().toMessage() shouldBe Ping("ping")
  }

  test("Pong message") {
    Pong().toJson() shouldEqualJson  """
      {
      "msg": "pong"
      }
    """.trimIndent()
  }

  test("Connect message") {
    """
      {
          "msg": "connect",
          "version": "1",
          "support": ["1"]
      }
    """.trimIndent().toMessage() shouldBe ConnectMessage("connect", "1", listOf("1"))
  }
})
