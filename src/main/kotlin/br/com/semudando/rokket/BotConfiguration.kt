package br.com.semudando.rokket

public data class BotConfiguration(
  val host: String,
  val port: Int,
  val username: String,
  val sha256Password: String,
) {
  public val apiUrl: String = "ws://$host:$port/websocket"
}
