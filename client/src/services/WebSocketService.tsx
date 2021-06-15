import React from "react";
import MessageWS, {ClientMessageWSType, ServerMessageWSType} from "models/MessageWS";
import CanvasService from "services/CanvasService";
import Position from "models/Position";
import Game from "models/Game";
import GameStartModal from "components/modal/GameStartModal";
import GameEndModal from "components/modal/GameEndModal";

class WebSocketService {
    webSocket: WebSocket;

    constructor() {
        const protocol = window.location.protocol === 'http:' ? 'ws://' : 'wss://';
        this.webSocket = new WebSocket(`${protocol}${location.hostname}:${location.port}/ws/game`);
    }

    init(showModal: Function, hideModal: Function) {
        this.sendPing()

        this.webSocket.onmessage = (messageEvent: MessageEvent) => {
            const messageWS = JSON.parse(messageEvent.data)

            switch (messageWS.type) {
                case ServerMessageWSType.GAME_START:
                    const startIn = JSON.parse(messageWS.data) as number
                    showModal(<GameStartModal hideModal={hideModal} startIn={startIn}/>)
                    break;
                case ServerMessageWSType.GAME:
                    const game = new Game(JSON.parse(messageWS.data))
                    CanvasService.drawGame(game)
                    break;
                case ServerMessageWSType.GAME_END:
                    const result = JSON.parse(messageWS.data)
                    showModal(<GameEndModal showModal={showModal} hideModal={hideModal} result={result}/>)
                    break;
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
    }

    sendNewGame1() {
        this.sendMessageWS(ClientMessageWSType.NEW_GAME, "GAME1")
    }

    sendNewGame2() {
        this.sendMessageWS(ClientMessageWSType.NEW_GAME, "GAME2")
    }

    sendMouseClick(x: number, y: number) {
        const data = JSON.stringify(new Position(x, y))
        this.sendMessageWS(ClientMessageWSType.MOUSE_CLICK, data)
    }

    sendMouseMove(x: number, y: number) {
        const data = JSON.stringify(new Position(x, y))
        this.sendMessageWS(ClientMessageWSType.MOUSE_MOVE, data)
    }
}

export default new WebSocketService();
