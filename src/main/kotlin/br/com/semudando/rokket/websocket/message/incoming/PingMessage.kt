package br.com.semudando.rokket.websocket.message.incoming

import br.com.semudando.rokket.websocket.message.Message

public data class PingMessage(
  override val msg: String,
) : Message {
  override val id: String = ""
}
