package br.com.semudando.rokket.websocket

data class RoomsGetMessage(val msg: String = "method", val method: String = "rooms/get", val id: String)
