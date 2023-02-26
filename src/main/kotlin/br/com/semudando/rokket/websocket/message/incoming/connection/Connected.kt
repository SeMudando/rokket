package br.com.semudando.rokket.websocket.message.incoming.connection

import br.com.semudando.rokket.websocket.message.Message

public data class Connected(
  override val msg: String,
  val session: String
) : Message
