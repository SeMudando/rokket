package br.com.semudando.rokket.websocket.message.outgoing

import br.com.semudando.rokket.websocket.message.Message

public data class ConnectMessage(
  override val msg: String = "connect",
  val version: String = "1",
  val support: List<String> = listOf("1"),
) : Message {
  override val id: String = ""
}
