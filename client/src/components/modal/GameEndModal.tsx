import React from "react";
import WebSocketService from "services/WebSocketService";
import WaitingPlayersModal from "components/modal/WaitingPlayersModal";
import styles from "./styles/gameEndModal.scss";

interface Props {
    showModal: Function
    hideModal: Function
    result: string
}

const GameEndModal = (props: Props) => {
    const {showModal, hideModal, result} = props

    const sendGame1 = () => {
        WebSocketService.sendNewGame1()
        hideModal()
    }

    const sendGame2 = () => {
        WebSocketService.sendNewGame2()
        showModal(<WaitingPlayersModal/>)
    }

    return (
        <div className={styles.container}>
            <div className={styles.title}>{result}</div>
            <div className={styles.buttons}>
                <button onClick={sendGame1}>
                    Game 1
                </button>
                <button onClick={sendGame2}>
                    Game 2
                </button>
            </div>
        </div>
    );
};

export default GameEndModal;
