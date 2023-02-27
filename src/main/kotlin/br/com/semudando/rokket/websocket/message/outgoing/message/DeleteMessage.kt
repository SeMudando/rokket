package br.com.semudando.rokket.websocket.message.outgoing.message

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class DeleteMessage(
  messageId: String,
  override val id: String = UUID.randomUUID().toString(),
) : Method("deleteMessage", listOf("_id" to messageId), id)
