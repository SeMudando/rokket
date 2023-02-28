package br.com.semudando.rokket.websocket.message.outgoing.message

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class StarMessage(
  messageId: String,
  roomId: String,
  enable: Boolean,
  override val id: String = UUID.randomUUID().toString(),
) : Method("starMessage", listOf(messageId, roomId, enable), id)
