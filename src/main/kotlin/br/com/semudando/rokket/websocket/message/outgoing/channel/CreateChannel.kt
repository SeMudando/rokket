package br.com.semudando.rokket.websocket.message.outgoing.channel

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class CreateChannel(
  channelName: String,
  readOnly: Boolean,
  usernames: List<String> = emptyList(),
  override val id: String = UUID.randomUUID().toString(),
) : Method("createChannel", listOf(channelName, readOnly, usernames), id)
