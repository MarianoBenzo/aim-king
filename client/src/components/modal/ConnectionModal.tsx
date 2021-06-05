import React, {useState} from "react";
import ChatService from "services/GameService";
import styles from "./styles/connectionModal.scss";

interface Props {
    hideModal: () => void
}

const ConnectionModal = (props: Props) => {
    const {hideModal} = props

    const [playerName, setPlayerName] = useState("");
    const [playerNameInvalid, setPlayerNameInvalid] = useState(false);

    const sendPlayerName = () => {
        if (!validatePlayerName(playerName)) {
            ChatService.sendNewPlayer(playerName)
            hideModal()
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
