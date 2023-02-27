package br.com.semudando.rokket.websocket.message.outgoing.settings

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class ListCustomEmoji(
  override val id: String = UUID.randomUUID().toString(),
) : Method("listEmojiCustom", emptyList(), id)
