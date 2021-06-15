import React from "react";
import GamesModal from "components/modal/GamesModal";
import styles from "./styles/gameEndModal.scss";

interface Props {
    showModal: Function
    hideModal: Function
    result: string
}

const GameEndModal = (props: Props) => {
    const {showModal, hideModal, result} = props

    const showGames = () => {
        showModal(<GamesModal showModal={showModal} hideModal={hideModal}/>)
    }

    return (
        <div className={styles.container}>
            <div className={styles.title}>{result}</div>
            <div className={styles.buttons}>
                <button onClick={showGames}>
                    Ok
                </button>
            </div>
        </div>
    );
};

export default GameEndModal;
