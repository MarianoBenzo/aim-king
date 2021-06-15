import Game from "models/Game";
import Target from "models/Target";
import Cursor from "models/Cursor";

class CanvasService {
    ctx: CanvasRenderingContext2D;

    constructor() {
        this.ctx = null;
    }

    initialize(canvas: HTMLCanvasElement) {
        canvas.style.background = '#eee';
        this.ctx = canvas.getContext('2d');
        this.drawBackground()
    }

    drawGame(game: Game) {
        if (this.ctx) {
            const height = this.ctx.canvas.height;
            const width = this.ctx.canvas.width;

            this.ctx.clearRect(0, 0, height, width);

            this.drawBackground();

            game.targets.forEach((target: Target) => {
                this.drawTarget(target);
            });

            game.cursors.forEach((cursor: Cursor) => {
                this.drawCursor(cursor);
            });
        }
    }

    drawBackground() {
        const height = this.ctx.canvas.height;
        const width = this.ctx.canvas.width;

        const fillColor = 'rgba(255, 255, 255)';
        const strokeColor = 'rgba(221, 221, 221)';

        this.ctx.fillStyle = fillColor;
        this.ctx.fillRect(0, 0, width, height);

        this.ctx.beginPath();
        for (let x = 0; x < width; x += 80) {
            this.ctx.moveTo(x, 0);
            this.ctx.lineTo(x, height);
        }
        this.ctx.moveTo(width, 0);
        this.ctx.lineTo(width, height);

        for (let y = 0; y < height; y += 80) {
            this.ctx.moveTo(0, y);
            this.ctx.lineTo(width, y);
        }
        this.ctx.moveTo(0, height);
        this.ctx.lineTo(width, height);

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
        this.ctx.lineWidth = 4;
        this.ctx.strokeStyle = '#006b8d';
        this.ctx.stroke();
    }

    drawCursor(cursor: Cursor) {
        // body
        this.ctx.beginPath();
        this.ctx.arc(
            cursor.position.x,
            cursor.position.y,
            6,
            0,
            2 * Math.PI,
            false
        );
        this.ctx.fillStyle = '#c60000';
        this.ctx.fill();
        this.ctx.lineWidth = 1;
        this.ctx.strokeStyle = '#8d0000';
        this.ctx.stroke();
    }
}

export default new CanvasService();
