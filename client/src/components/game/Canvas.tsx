import * as React from "react";
import {useEffect} from "react";
import CanvasService from "services/CanvasService";
import GameService from "services/GameService";

export const Canvas = () => {

    useEffect(() => {
        const canvas = document.getElementById("canvas") as HTMLCanvasElement;
        CanvasService.initialize(canvas);

        canvas.addEventListener("mousedown", (e: MouseEvent) => {
            sendMousePosition(canvas, e);
        });

        /*
        canvas.addEventListener("mousemove", (e: MouseEvent) => {
            getMousePosition(canvas, e);
        });
        */
    }, []);

    const sendMousePosition = (canvas, event) => {
        let rect = canvas.getBoundingClientRect();
        let x = event.clientX - rect.left;
        let y = event.clientY - rect.top;
        GameService.sendClickPosition(x, y)
    }

    return (
        <canvas id="canvas">Error</canvas>
    );
};
