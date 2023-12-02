"use client";
import styles from "@/app/components/css/coursereview.module.css";
interface ReviewProps{
   quality:number
   difficulty:number
    comment:string
    date:string
    
}

function Review({quality,difficulty,comment,date}:ReviewProps) {
    return (
        <div>
            <div className={styles.Review}>
                <div className={styles.RatingNumbersBox}>
                    <div className={styles.Quality}>
                        <div className={styles.RatingHeader}>Quality</div>
                        <div className={styles.RatingNumber}>{quality}</div>
                    </div>
                    <div className={styles.Difficulty}>
                        <div className={styles.RatingHeader}>Difficulty</div>
                        <div className={styles.RatingNumber}>{difficulty}</div>
                    </div>
                </div>

                <div className={styles.main}>
                    <div className={styles.Options}>
                        <div className={styles.option}>
                            <div className={styles.optionheader}>For credit:</div>
                            <div className={styles.optionval}>Yes</div>
                        </div>
                        <div className={styles.option}>
                            <div className={styles.optionheader}>Attendance:</div>
                            <div className={styles.optiontext}>Mandatory</div>
                        </div>
                        <div className={styles.option}>
                            <div className={styles.optionheader}>Grade:</div>
                            <div className={styles.optiontext}>Not sure yet</div>
                        </div>
                        <div className={styles.option}>
                            <div className={styles.optionheader}>Textbook:</div>
                            <div className={styles.optiontext}>N/A</div>
                        </div>
                        <div className={styles.date}>{date}</div>

                    </div>

                    <div className={styles.content}>{comment}</div>

                </div>
            </div>
        </div >

    );
}

export default Review;