package br.com.semudando.rokket.websocket.message.outgoing.room

import br.com.semudando.rokket.websocket.message.outgoing.Method
import java.util.UUID

public class FavoriteRoom(
  roomId: String,
  favorite: Boolean = true,
  override val id: String = UUID.randomUUID().toString(),
) : Method("toggleFavorite", listOf(roomId, favorite), id)
