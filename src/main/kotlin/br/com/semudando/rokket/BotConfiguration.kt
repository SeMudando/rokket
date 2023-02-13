package br.com.semudando.rokket

public data class BotConfiguration(
  val host: String,
  val username: String,
  val password: String,
  val ignoredChannels: List<String>,
  val botId: String,
  val webservicePort: Int,
)
