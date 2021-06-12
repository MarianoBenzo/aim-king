import * as React from "react";
import {useEffect} from "react";
import CanvasService from "services/CanvasService";
import WebSocketService from "services/WebSocketService";
import CanvasResolution from "utils/CanvasResolution";

export const Canvas = () => {
    const height = CanvasResolution.height;
    const width = CanvasResolution.width;
    const scala = 2;

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
        let x = (event.clientX - rect.left) * scala;
        let y = (event.clientY - rect.top) * scala;
        WebSocketService.sendClickPosition(x, y)
    }

    const style = {
        height: `${height / scala}px`,
        width: `${width / scala}px`
    }

    return (
        <canvas id="canvas"
                height={height}
                width={width}
                style={style}>
            Error
        </canvas>
    );
};
