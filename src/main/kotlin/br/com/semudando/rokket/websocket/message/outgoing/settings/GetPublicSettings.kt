package br.com.semudando.rokket.websocket.message.outgoing.settings

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class GetPublicSettings(
  override val id: String = UUID.randomUUID().toString(),
) : Method("public-settings/get", emptyList(), id)
