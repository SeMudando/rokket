package br.com.semudando.rokket.websocket.message.outgoing

import br.com.semudando.rokket.websocket.message.Message

public data class LoginMessage(
  val params: List<WebserviceRequestParam>,
  override val msg: String = "method",
  val method: String = "login",
  override val id: String = "login",
) : Message {
  public constructor(param: WebserviceRequestParam) : this(listOf(param))
}

public data class WebserviceRequestParam(val user: UserData, val password: PasswordData)

public data class UserData(val username: String)

public data class PasswordData(val digest: String, val algorithm: String = "sha-256")
