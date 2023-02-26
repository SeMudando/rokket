package br.com.semudando.rokket.websocket.message.incoming

import br.com.semudando.rokket.websocket.message.Message

public interface Response : Message {
  public val result: Any?
  public val error: Any?
  public val id: String
}
