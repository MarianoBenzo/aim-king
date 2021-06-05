import Position from "models/Position";

export default class Target {
	position: Position
	radius: number

	constructor(target: any) {
		this.position = new Position(target.position.x, target.position.y);
		this.radius = target.radius
	}
}