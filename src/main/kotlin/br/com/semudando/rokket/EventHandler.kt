package br.com.semudando.rokket

public interface EventHandler {
  public fun handleRoomMessage(channel: Channel, user: User, message: Message): List<OutgoingMessage>

  public fun handleOwnMessage(channel: Channel, user: User, message: Message): List<OutgoingMessage>

  public fun botInitialized()

  public data class Channel(val id: String, val name: String?, val type: ChannelType)

  public data class User(val id: String, val username: String)

  public data class Message(val message: String, val botMessage: Boolean)

  public enum class ChannelType {
    CHANNEL,
    DIRECT,
    OTHER
  }
}
