import * as React from "react";
import {useContext, useEffect} from "react";
import CanvasService from "services/CanvasService";
import GameService from "services/GameService";
import {GameContext} from "components/game/GameProvider";

export const Canvas = () => {
    const {height, width} = useContext(GameContext)

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
        <canvas id="canvas" height={height} width={width}>Error</canvas>
    );
};
