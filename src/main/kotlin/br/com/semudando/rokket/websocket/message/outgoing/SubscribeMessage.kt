package br.com.semudando.rokket.websocket.message.outgoing

public data class SubscribeMessage(
  val msg: String = "sub",
  val id: String,
  val name: String,
  val params: List<Any?>,
)
