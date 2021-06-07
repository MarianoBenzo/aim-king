import * as React from "react";
import {useEffect, useState} from "react";
import GameService from "services/GameService";

interface Props {
    children: JSX.Element[] | JSX.Element
}

export interface GameContextProps {
    height: number;
    width: number;
}

export const GameContext = React.createContext<GameContextProps>({
    height: null,
    width: null
});

const GameProvider = (props: Props) => {

    const {children} = props;

    const [height, setHeight] = useState(0);
    const [width, setWidth] = useState(0);

    useEffect(() => {
        GameService.init(setHeight, setWidth);
    }, []);

    const context: GameContextProps = {
        height: height,
        width: width
    };

    return (
        <GameContext.Provider value={context}>
            {children}
        </GameContext.Provider>
    )
};

export default GameProvider;
