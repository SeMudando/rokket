package br.com.semudando.rokket.websocket.message.outgoing

import br.com.semudando.rokket.websocket.message.Message

public interface Notification : Message {
  public val method: String
  public val params: List<Any>
}
