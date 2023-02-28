package br.com.semudando.rokket.websocket.message.outgoing.message

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class SendMessage(
  messageId: String,
  roomId: String,
  msg: String,
  override val id: String = UUID.randomUUID().toString(),
) : Method("sendMessage", listOf(messageId, roomId, msg), id)
