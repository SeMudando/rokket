package br.com.semudando.rokket.websocket.message.outgoing.subscriptions

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import java.util.UUID

public class GetSubscriptions(
  date: LocalDateTime,
  override val id: String = UUID.randomUUID().toString(),
) : Method("subscriptions/get", listOf("\$date" to date.toEpochSecond(UTC)), id)
