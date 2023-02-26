package br.com.semudando.rokket.websocket.message.outgoing

import br.com.semudando.rokket.websocket.message.Message

public interface Request : Message {
  public val method: String
  public val params: List<Any>
  public val id: String
}

