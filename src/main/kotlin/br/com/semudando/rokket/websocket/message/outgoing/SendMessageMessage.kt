package br.com.semudando.rokket.websocket.message.outgoing

public data class SendMessageMessage(
  val msg: String = "method",
  val method: String = "sendMessage",
  val id: String,
  val params: List<Map<String, Any>>,
)
