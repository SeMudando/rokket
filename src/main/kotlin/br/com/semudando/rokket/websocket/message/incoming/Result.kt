package br.com.semudando.rokket.websocket.message.incoming

public open class Result : Response {
  override val msg: String = "result"
  override val id: String = ""
  override val result: Any? = Any()
  override val error: Any? = null
}

