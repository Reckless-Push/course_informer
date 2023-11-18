import React, { Component } from "react";
import styles from "./css/coursereview.module.css";

class Review extends Component {
    render() {
        return (
            <div>
                <div className={styles.Review}>
                    <div className={styles.RatingNumbersBox}>
                        <div className={styles.Quality}>
                            <div className={styles.RatingHeader}>Quality</div>
                            <div className={styles.RatingNumber}>4.0</div>
                        </div>
                        <div className={styles.Difficulty}>
                            <div className={styles.RatingHeader}>Difficulty</div>
                            <div className={styles.RatingNumber}>3.0</div>
                        </div>
                    </div>
                    {/*   */}
                </div>
            </div >

        );
    }
}

export default Review;