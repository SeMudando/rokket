package br.com.semudando.rokket.websocket.message.outgoing

public data class LoginMessage(
  val msg: String = "method",
  val method: String = "login",
  val id: String,
  val params: List<WebserviceRequestParam>,
)

public data class WebserviceRequestParam(val user: UserData, val password: PasswordData)

public data class UserData(val username: String)

public data class PasswordData(val digest: String, val algorithm: String)
