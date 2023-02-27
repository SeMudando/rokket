package br.com.semudando.rokket.websocket.message.outgoing.room

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class LeaveRoom(
  roomId: String,
  override val id: String = UUID.randomUUID().toString(),
) : Method("leaveRoom", listOf(roomId), id)
