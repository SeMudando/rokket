package br.com.semudando.rokket.websocket.message.outgoing

public data class ConnectMessage(
  val msg: String = "connect",
  val version: String = "1",
  val support: List<String> = listOf("1"),
)
