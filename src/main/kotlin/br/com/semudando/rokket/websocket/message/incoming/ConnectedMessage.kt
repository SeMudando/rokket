package br.com.semudando.rokket.websocket.message.incoming

import br.com.semudando.rokket.websocket.message.Message

public data class ConnectedMessage(
  override val msg: String,
  val session: String
) : Message {
  override val id: String = ""
}
