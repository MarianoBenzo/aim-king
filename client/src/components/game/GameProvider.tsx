import * as React from "react";

interface Props {
    children: JSX.Element[] | JSX.Element
}

export interface GameContextProps {
}

export const GameContext = React.createContext<GameContextProps>({
});

const GameProvider = (props: Props) => {

    const {children} = props;

    const context: GameContextProps = {
    };

    return (
        <GameContext.Provider value={context}>
            {children}
        </GameContext.Provider>
    )
};

export default GameProvider;
