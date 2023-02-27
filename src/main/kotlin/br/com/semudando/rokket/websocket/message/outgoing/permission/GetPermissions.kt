package br.com.semudando.rokket.websocket.message.outgoing.permission

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class GetPermissions(
  override val id: String = UUID.randomUUID().toString(),
) : Method("permissions/get", emptyList())
