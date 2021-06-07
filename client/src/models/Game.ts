import Target from "models/Target";

export default class Game {
	height: number;
	width: number;
	targets: Array<Target>;

	constructor(game: any) {
		this.height = game.height;
		this.width = game.width;
		this.targets = [];
		game.targets.forEach(target =>
			this.targets.push(new Target(target))
		)
	}
}