package br.com.semudando.rokket.websocket.message.outgoing.room

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class SaveRoomSettings(
  roomId: String,
  setting: String,
  value: String,
  override val id: String = UUID.randomUUID().toString(),
) : Method("saveRoomSettings", listOf(roomId, setting, value), id)
