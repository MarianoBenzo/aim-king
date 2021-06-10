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
	NEW_GAME1 = "NEW_GAME1",
	NEW_GAME2 = "NEW_GAME2",
	CLICK = "CLICK"
}

export enum ServerMessageWSType {
	GAME = "GAME"
}
