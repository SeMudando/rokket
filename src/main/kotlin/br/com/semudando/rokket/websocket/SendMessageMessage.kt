package br.com.semudando.rokket.websocket

data class SendMessageMessage(
  val msg: String = "method",
  val method: String = "sendMessage",
  val id: String,
  val params: List<Map<String, Any>>,
)
