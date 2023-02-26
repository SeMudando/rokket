package br.com.semudando.rokket.websocket.message.outgoing.connection

import br.com.semudando.rokket.websocket.message.Message

public data class ConnectMessage(
  override val msg: String = "connect",
  val version: String = "1",
  val support: List<String> = listOf("1"),
) : Message
