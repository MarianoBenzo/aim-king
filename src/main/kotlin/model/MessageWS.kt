package model

class MessageWS(val type: String, val data: String?) {
    constructor(type: ServerMessageWSType, data: String?) : this(type.name, data)
}

enum class ClientMessageWSType {
    PING,
    NEW_PLAYER,
    NEW_GAME,
    MOUSE_CLICK,
    MOUSE_MOVE
}

enum class ServerMessageWSType {
    PONG,
    GAME_START,
    GAME,
    GAME_END
}
