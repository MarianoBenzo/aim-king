import Game from "models/Game";
import Target from "models/Target";

class CanvasService {
    ctx: CanvasRenderingContext2D;
    width: number;
    height: number;

    constructor() {
        this.ctx = null;
        this.width = 800;
        this.height = 500;
    }

    initialize(canvas: HTMLCanvasElement) {
        canvas.style.background = '#eee';
        canvas.width = this.width;
        canvas.height = this.height;

        this.ctx = canvas.getContext('2d');
    }

    drawGame(game: Game) {
        if (this.ctx) {
            this.ctx.clearRect(0, 0, this.width, this.height);

            this.drawBackground();

            game.targets.forEach((target: Target) => {
                this.drawTarget(target);
            });
        }
    }

    drawBackground() {
        const fillColor = 'rgba(255, 255, 255)';
        const strokeColor = 'rgba(221, 221, 221)';

        this.ctx.fillStyle = fillColor;
        this.ctx.fillRect(0, 0, this.width, this.height);

        this.ctx.beginPath();
        for (let x = 0; x < this.width; x += 40) {
            this.ctx.moveTo(x, 0);
            this.ctx.lineTo(x, this.height);
        }
        this.ctx.moveTo(this.width, 0);
        this.ctx.lineTo(this.width, this.height);

        for (let y = 0; y < this.height; y += 40) {
            this.ctx.moveTo(0, y);
            this.ctx.lineTo(this.width, y);
        }
        this.ctx.moveTo(0, this.height);
        this.ctx.lineTo(this.width, this.height);

        this.ctx.closePath();

        this.ctx.strokeStyle = strokeColor;
        this.ctx.lineWidth = 1;
        this.ctx.stroke();
    }

    drawTarget(target: Target) {
        // body
        this.ctx.beginPath();
        this.ctx.arc(
            target.position.x,
            target.position.y,
            target.radius,
            0,
            2 * Math.PI,
            false
        );
        this.ctx.fillStyle = '#0098C67F';
        this.ctx.fill();
        this.ctx.lineWidth = 2;
        this.ctx.strokeStyle = '#006b8d';
        this.ctx.stroke();
    }
}

export default new CanvasService();
