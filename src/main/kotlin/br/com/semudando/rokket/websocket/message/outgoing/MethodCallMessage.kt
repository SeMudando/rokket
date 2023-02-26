package br.com.semudando.rokket.websocket.message.outgoing

import br.com.semudando.rokket.websocket.message.Message

public interface MethodCallMessage : Message {
  override val msg: String
    get() = "method"
  public val method: String
  public val params: List<Any>
    get() = emptyList()
}
