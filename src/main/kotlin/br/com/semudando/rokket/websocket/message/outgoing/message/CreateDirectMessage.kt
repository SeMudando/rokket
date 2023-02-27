package br.com.semudando.rokket.websocket.message.outgoing.message

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class CreateDirectMessage(
  username: String,
  override val id: String = UUID.randomUUID().toString(),
) : Method("createDirectMessage", listOf(username), id)
