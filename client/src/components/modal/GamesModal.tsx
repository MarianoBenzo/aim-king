import React from "react";
import WebSocketService from "services/WebSocketService";
import styles from "./styles/gamesModal.scss";
import WaitingPlayersModal from "components/modal/WaitingPlayersModal";

interface Props {
    showModal: Function
    hideModal: Function
}

const GamesModal = (props: Props) => {
    const {showModal, hideModal} = props

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
            <div className={styles.title}>Play</div>
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

export default GamesModal;
