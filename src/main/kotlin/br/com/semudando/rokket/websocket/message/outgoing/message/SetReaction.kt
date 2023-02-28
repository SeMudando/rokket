package br.com.semudando.rokket.websocket.message.outgoing.message

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class SetReaction(
  emoji: String,
  messageId: String,
  enable: Boolean,
  override val id: String = UUID.randomUUID().toString(),
) : Method("setReaction", listOf(emoji, messageId, enable), id)
