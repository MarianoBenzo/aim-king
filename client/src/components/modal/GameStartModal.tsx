import React, {useEffect} from "react";
import styles from "./styles/gameStartModal.scss";

interface Props {
    hideModal: Function
    startIn: number
}

const GameStartModal = (props: Props) => {
    const {hideModal, startIn} = props

    useEffect(() => {
        setTimeout(hideModal, startIn)
    }, []);

    return (
        <div className={styles.container}>
            <div className={styles.title}>The game start in 3 seconds</div>
        </div>
    );
};

export default GameStartModal;
