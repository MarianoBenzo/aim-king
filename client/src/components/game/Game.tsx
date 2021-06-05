import React, {useEffect} from "react";
import {useContext} from "react";
import {ModalContext} from "components/modal/ModalProvider";
import {GameContext} from "components/game/GameProvider";
import ConnectionModal from "components/modal/ConnectionModal";
import styles from "./styles/game.scss";
import {Canvas} from "components/game/Canvas";

const Game = React.memo(() => {
    const {showModal, hideModal} = useContext(ModalContext)
    const {game} = useContext(GameContext); //TODO

    useEffect(() => {
        showModal(<ConnectionModal hideModal={hideModal}/>)
    }, []);

    return (
        <div className={styles.container}>
            <Canvas/>
        </div>
    );
});

export default Game;
