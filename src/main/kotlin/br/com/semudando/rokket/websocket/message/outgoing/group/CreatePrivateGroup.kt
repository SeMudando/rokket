package br.com.semudando.rokket.websocket.message.outgoing.group

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class CreatePrivateGroup(
  channelName: String,
  usernames: List<String> = emptyList(),
  override val id: String = UUID.randomUUID().toString(),
) : Method("createPrivateGroup", listOf(channelName, usernames), id)
