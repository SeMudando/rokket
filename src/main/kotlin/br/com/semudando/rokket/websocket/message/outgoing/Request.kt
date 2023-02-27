package br.com.semudando.rokket.websocket.message.outgoing

import br.com.semudando.rokket.websocket.message.Message
import java.util.UUID

public interface Request : Message {
  public val method: String
  public val params: List<Any>
  public val id: String
}

public open class Method(
  override val method: String,
  override val params: List<Any> = emptyList(),
  override val id: String = UUID.randomUUID().toString(),
) : Request {
  override val msg: String = "method"
}
