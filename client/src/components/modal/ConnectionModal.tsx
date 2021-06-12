import React, {useState} from "react";
import WebSocketService from "services/WebSocketService";
import styles from "./styles/connectionModal.scss";
import GamesModal from "components/modal/GamesModal";

interface Props {
    hideModal: Function
    showModal: Function
}

const ConnectionModal = (props: Props) => {
    const {showModal, hideModal} = props

    const [playerName, setPlayerName] = useState("");
    const [playerNameInvalid, setPlayerNameInvalid] = useState(false);

    const sendPlayerName = () => {
        if (!validatePlayerName(playerName)) {
            WebSocketService.sendNewPlayer(playerName)
            showModal(<GamesModal showModal={showModal} hideModal={hideModal}/>)
        } else {
            setPlayerNameInvalid(true)
        }
    }

    const validatePlayerName = (playerName: string): boolean => {
        return playerName.trim().length === 0
    }

    const inputOnChange = (e) => {
        const validPlayerName = e.target.value.slice(0, 40)
        setPlayerName(validPlayerName)
        setPlayerNameInvalid(validatePlayerName(validPlayerName))
    }

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            sendPlayerName()
        }
    }

    return (
        <div className={styles.container}>
            <div className={styles.title}>Welcome!</div>
            <div className={styles.playerNameInvalid}>{playerNameInvalid ? "Player name invalid" : ""}</div>
            <div className={`${styles.playerNameInput} ${playerNameInvalid ? styles.playerNameInvalidInput : ""}`}>
                <input type="text"
                       value={playerName}
                       placeholder="Enter your player name here"
                       onChange={inputOnChange}
                       onKeyDown={handleKeyDown}/>

                <button onClick={sendPlayerName}>
                    Done
                </button>
            </div>
        </div>
    );
};

export default ConnectionModal;
