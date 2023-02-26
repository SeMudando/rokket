package br.com.semudando.rokket.websocket.message.incoming

import br.com.semudando.rokket.websocket.message.Message

public open class ResultMessage : Message {
  override val msg: String = "result"
  override val id: String = ""
  public open val result: Any = Any()
}

