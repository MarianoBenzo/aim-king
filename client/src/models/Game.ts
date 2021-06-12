import Target from "models/Target";

export default class Game {
	targets: Array<Target>;

	constructor(game: any) {
		this.targets = [];
		game.targets.forEach(target =>
			this.targets.push(new Target(target))
		)
	}
}