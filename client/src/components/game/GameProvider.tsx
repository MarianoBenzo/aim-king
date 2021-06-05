import * as React from "react";
import {useEffect, useState} from "react";
import Game from "models/Game";
import GameService from "services/GameService";

interface Props {
    children: JSX.Element[] | JSX.Element
}

export interface GameContextProps {
    game: Game;
}

export const GameContext = React.createContext<GameContextProps>({
    game: null
});

const GameProvider = (props: Props) => {

    const {children} = props;

    const [game, setGame] = useState(null); //TODO

    useEffect(() => {
        GameService.init();
    }, []);

    const context: GameContextProps = {
        game: game
    };

    return (
        <GameContext.Provider value={context}>
            {children}
        </GameContext.Provider>
    )
};

export default GameProvider;
