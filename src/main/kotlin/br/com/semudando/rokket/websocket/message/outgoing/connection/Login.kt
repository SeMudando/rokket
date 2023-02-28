package br.com.semudando.rokket.websocket.message.outgoing.connection

import br.com.semudando.rokket.websocket.message.outgoing.Method

public class Login(
  username: String,
  sha256Digest: String,
) : Method("login", listOf(WebserviceRequestParam(UserData(username), PasswordData(sha256Digest))), "login")

private data class WebserviceRequestParam(val user: UserData, val password: PasswordData)

private data class UserData(val username: String)

private data class PasswordData(val digest: String, val algorithm: String = "sha-256")
