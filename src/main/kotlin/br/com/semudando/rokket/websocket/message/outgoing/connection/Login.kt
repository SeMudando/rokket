package br.com.semudando.rokket.websocket.message.outgoing.connection

import br.com.semudando.rokket.websocket.message.outgoing.Request

public data class Login(
  override val params: List<WebserviceRequestParam>,
  override val msg: String = "method",
  override val method: String = "login",
  override val id: String = "login",
) : Request {
  public constructor(param: WebserviceRequestParam) : this(listOf(param))
}

public data class WebserviceRequestParam(val user: UserData, val password: PasswordData)

public data class UserData(val username: String)

public data class PasswordData(val digest: String, val algorithm: String = "sha-256")
