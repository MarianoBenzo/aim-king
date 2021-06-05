import React from "react";
import ModalProvider from "components/modal/ModalProvider";
import GameProvider from "components/game/GameProvider";
import Game from "./Game";

const GameWrapper = () => {
    return (
        <ModalProvider>
            <GameProvider>
                <Game/>
            </GameProvider>
        </ModalProvider>
    );
};

export default GameWrapper;
