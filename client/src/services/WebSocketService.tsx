import React from "react";
import MessageWS, {ClientMessageWSType, ServerMessageWSType} from "models/MessageWS";
import CanvasService from "services/CanvasService";
import Position from "models/Position";
import Game from "models/Game";

class WebSocketService {
    webSocket: WebSocket;

    constructor() {
        const protocol = window.location.protocol === 'http:' ? 'ws://' : 'wss://';
        this.webSocket = new WebSocket(`${protocol}${location.hostname}:${location.port}/ws/game`);
    }

    init() {
        this.sendPing()

        this.webSocket.onmessage = (messageEvent: MessageEvent) => {
            const messageWS = JSON.parse(messageEvent.data)

            switch (messageWS.type) {
                case ServerMessageWSType.GAME:
                    const game = new Game(JSON.parse(messageWS.data))
                    CanvasService.drawGame(game)
                    break;
                case ServerMessageWSType.GAME_END:
                    const time = JSON.parse(messageWS.data)
                    console.log("Time: ", time)
            }
        }

        this.webSocket.onclose = () => {
           alert("Server Disconnect You")
        }
    }

    private sendMessageWS(type: ClientMessageWSType, data?: string) {
        this.webSocket.send(JSON.stringify(new MessageWS(type, data)));
    }

    private sendPing() {
        setInterval(() => this.sendMessageWS(ClientMessageWSType.PING), 30000);
    }

    sendNewPlayer(playerName: string) {
        this.sendMessageWS(ClientMessageWSType.NEW_PLAYER, playerName)
        this.sendNewGame1()
    }

    sendNewGame1() {
        this.sendMessageWS(ClientMessageWSType.NEW_GAME1)
    }

    sendNewGame2() {
        this.sendMessageWS(ClientMessageWSType.NEW_GAME2)
    }

    sendClickPosition(x: number, y: number) {
        const data = JSON.stringify(new Position(x, y))
        this.sendMessageWS(ClientMessageWSType.CLICK, data)
    }
}

export default new WebSocketService();
