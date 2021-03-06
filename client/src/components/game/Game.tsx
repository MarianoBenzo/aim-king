import React, {useEffect} from "react";
import {useContext} from "react";
import {ModalContext} from "components/modal/ModalProvider";
import ConnectionModal from "components/modal/ConnectionModal";
import styles from "./styles/game.scss";
import {Canvas} from "components/game/Canvas";
import WebSocketService from "services/WebSocketService";

const Game = React.memo(() => {
    const {showModal, hideModal} = useContext(ModalContext)

    useEffect(() => {
        WebSocketService.init(showModal, hideModal);
    }, []);

    useEffect(() => {
        showModal(<ConnectionModal showModal={showModal} hideModal={hideModal}/>)
    }, []);

    return (
        <div className={styles.container}>
            <Canvas/>
        </div>
    );
});

export default Game;
