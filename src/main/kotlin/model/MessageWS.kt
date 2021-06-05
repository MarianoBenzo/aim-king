package model

class MessageWS(val type: String, val data: String?) {
    constructor(type: ClientMessageWSType, data: String?) : this(type.name, data)//TODO
    constructor(type: ServerMessageWSType, data: String?) : this(type.name, data)
}

enum class ClientMessageWSType {
    PING,
    NEW_PLAYER,
    CLICK
}

enum class ServerMessageWSType {
    PONG,
    GAME
}
