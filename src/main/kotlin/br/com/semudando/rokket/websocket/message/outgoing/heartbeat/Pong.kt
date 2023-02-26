package br.com.semudando.rokket.websocket.message.outgoing.heartbeat

import br.com.semudando.rokket.websocket.message.Message

public data class Pong(override val msg: String = "pong") : Message
