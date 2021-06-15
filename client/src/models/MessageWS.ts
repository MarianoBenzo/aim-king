export default class MessageWS {
	type: string;
	data: any;

	constructor(type: ClientMessageWSType | ServerMessageWSType, data?: string) {
		this.type = type;
		this.data = data;
	}
}

export enum ClientMessageWSType {
	PING = "PING",
	NEW_PLAYER = "NEW_PLAYER",
	NEW_GAME = "NEW_GAME",
	MOUSE_CLICK = "MOUSE_CLICK",
	MOUSE_MOVE = "MOUSE_MOVE"
}

export enum ServerMessageWSType {
	GAME_START = "GAME_START",
	GAME = "GAME",
	GAME_END = "GAME_END"
}
