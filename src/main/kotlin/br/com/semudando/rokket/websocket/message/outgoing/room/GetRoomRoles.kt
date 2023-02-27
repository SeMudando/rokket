package br.com.semudando.rokket.websocket.message.outgoing.room

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class GetRoomRoles(
  roomId: String,
  override val id: String = UUID.randomUUID().toString()
) : Method("getRoomRoles", listOf(roomId), id)
