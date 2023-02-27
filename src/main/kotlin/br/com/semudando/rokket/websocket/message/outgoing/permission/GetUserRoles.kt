package br.com.semudando.rokket.websocket.message.outgoing.permission

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class GetUserRoles(
  override val id: String = UUID.randomUUID().toString(),
) : Method("getUserRoles", emptyList(), id)
