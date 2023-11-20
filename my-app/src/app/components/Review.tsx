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

                    <div className={styles.main}>
                        <div className={styles.Options}>
                            <div className={styles.ForCredit}>
                                <div className={styles.optionheader}>For credit:</div>
                                <div className={styles.optionval}>Yes</div>
                            </div>
                            <div className={styles.ForCredit}>
                                <div className={styles.optionheader}>Attendance:</div>
                                <div className={styles.optiontext}>Mandatory</div>
                            </div>
                            <div className={styles.ForCredit}>
                                <div className={styles.optionheader}>Grade:</div>
                                <div className={styles.optiontext}>Not sure yet</div>
                            </div>
                            <div className={styles.ForCredit}>
                                <div className={styles.optionheader}>Textbook:</div>
                                <div className={styles.optiontext}>N/A</div>
                            </div>
                            <div className={styles.date}>Apr 21st, 2023</div>

                        </div>

                        <div className={styles.content}>
                            Professor Conboy is a kind and understanding instructor who cares about her students
                            and the material she teaches. However, she has high expectations for grading. While
                            the workload isn't terrible, it can be very easy to lose a lot of credit on the homework
                            assignments due to unclear instructions.
                        </div>

                    </div>
                </div>
            </div >

        );
    }
}

export default Review;