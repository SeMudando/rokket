package br.com.semudando.rokket.websocket.message.outgoing

import java.util.UUID

public open class Method(
    override val method: String,
    override val params: List<Any> = emptyList(),
    override val id: String = UUID.randomUUID().toString(),
) : Request {
  override val msg: String = "method"
}
