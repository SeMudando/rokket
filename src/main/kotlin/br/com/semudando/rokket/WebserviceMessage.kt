package br.com.semudando.rokket

data class WebserviceMessage(
  val roomId: String?,
  val roomName: String?,
  val message: String,
  val emoji: String? = null,
  val username: String? = null,
)
