package br.com.semudando.rokket.websocket.message.outgoing

import br.com.semudando.rokket.websocket.message.Message
import com.fasterxml.jackson.annotation.JsonIgnore

public data class PongMessage(override val msg: String = "pong") : Message {

  @get:JsonIgnore
  override val id: String
    get() = ""
}
