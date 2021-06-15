import Position from "models/Position";

export default class Cursor {
	position: Position

	constructor(target: any) {
		this.position = new Position(target.position.x, target.position.y);
	}
}