import * as React from "react";
import {useEffect, useState} from "react";
import CanvasService from "services/CanvasService";
import WebSocketService from "services/WebSocketService";
import CanvasResolution from "utils/CanvasResolution";

export const Canvas = () => {
    const height = CanvasResolution.height;
    const width = CanvasResolution.width;
    const scala = 2;

    const [moveSent, setMoveSent] = useState(false);

    useEffect(() => {
        const canvas = document.getElementById("canvas") as HTMLCanvasElement;
        CanvasService.initialize(canvas);
    }, []);

    const sendMouseClick = (event: React.MouseEvent<HTMLCanvasElement>) => {
        const canvas = document.getElementById("canvas") as HTMLCanvasElement;
        let rect = canvas.getBoundingClientRect();
        let x = (event.clientX - rect.left) * scala;
        let y = (event.clientY - rect.top) * scala;
        WebSocketService.sendMouseClick(x, y)
    }

    const sendMouseMove = (event: React.MouseEvent<HTMLCanvasElement>) => {
        if(!moveSent) {
            setMoveSent(true)
            const canvas = document.getElementById("canvas") as HTMLCanvasElement;
            let rect = canvas.getBoundingClientRect();
            let x = (event.clientX - rect.left) * scala;
            let y = (event.clientY - rect.top) * scala;
            WebSocketService.sendMouseMove(x, y)
            setTimeout(() => setMoveSent(false), 30)
        }
    }

    const style = {
        height: `${height / scala}px`,
        width: `${width / scala}px`
    }

    return (
        <canvas id="canvas"
                height={height}
                width={width}
                style={style}
                onMouseDown={(event: React.MouseEvent<HTMLCanvasElement>) => sendMouseClick(event)}
                onMouseMove={(event: React.MouseEvent<HTMLCanvasElement>) => sendMouseMove(event)}>
            Error
        </canvas>
    );
};
