package br.com.semudando.rokket.websocket.message

import br.com.semudando.rokket.websocket.message.incoming.connection.Connected
import br.com.semudando.rokket.websocket.message.incoming.heartbeat.Ping
import br.com.semudando.rokket.websocket.message.incoming.Result
import br.com.semudando.rokket.websocket.message.outgoing.connection.ConnectMessage
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.websocket.Frame
import io.ktor.websocket.Frame.Text
import io.ktor.websocket.readText

private val mapper = ObjectMapper().registerKotlinModule()

internal fun Message.toJson() = mapper.writeValueAsString(this)

internal fun Frame.toMessage() = (this as Text).readText().toMessage()

internal fun String.toMessage(): Message? {
  val tree = mapper.readTree(this)

  return when(tree.msg) {
    "connect" -> mapper.readValue<ConnectMessage>(this)
    "connected" -> mapper.readValue<Connected>(this)
    "ping" -> mapper.readValue<Ping>(this)
    "result" -> mapper.readValue<Result>(this)
    else -> {
      println(tree.toPrettyString())
      return null
    }
  }
}

private val JsonNode.msg
  get() = this["msg"]?.textValue()
