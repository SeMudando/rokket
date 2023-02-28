package br.com.semudando.rokket.websocket.message.outgoing.user

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class RegisterUser(
  email: String,
  pass: String,
  name: String,
  secretUrl: String? = null,
  override val id: String = UUID.randomUUID().toString(),
) : Method("registerUser", listOf(mapOf(
  "email" to email, "pass" to pass, "name" to name, "secretURL" to secretUrl
).filterValues { it != null }), id)
