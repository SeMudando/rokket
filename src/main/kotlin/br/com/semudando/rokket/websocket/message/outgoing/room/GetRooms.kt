package br.com.semudando.rokket.websocket.message.outgoing.room

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import java.util.UUID

public class GetRooms(
  date: LocalDateTime,
  override val id: String = UUID.randomUUID().toString(),
) : Method("rooms/get", listOf("\$date" to date.toEpochSecond(UTC)), id)
