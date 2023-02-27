package br.com.semudando.rokket.websocket.message.outgoing.channel

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class JoinChannel(
  roomId: String,
  joinCode: String? = null,
  override val id: String = UUID.randomUUID().toString(),
) : Method("joinRoom", listOfNotNull(roomId, joinCode), id)
