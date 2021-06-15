import Target from "models/Target";
import Cursor from "models/Cursor";

export default class Game {
	targets: Array<Target>;
	cursors: Array<Cursor>;

	constructor(game: any) {
		this.targets = [];
		game.targets.forEach(target =>
			this.targets.push(new Target(target))
		)
		this.cursors = [];
		game.cursors.forEach(cursor =>
			this.cursors.push(new Cursor(cursor))
		)
	}
}