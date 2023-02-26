package br.com.semudando.rokket.websocket.message.incoming.heartbeat

import br.com.semudando.rokket.websocket.message.Message

public data class Ping(
  override val msg: String,
) : Message
