"use client";
import styles from "@/app/components/css/coursereview.module.css";
import { Review } from "@/types/review";

function Review(data: Review) {
  return (
    <div>
      <div className={styles.Review}>
        <div className={styles.RatingNumbersBox}>
          <div className={styles.Quality}>
            <div className={styles.RatingHeader}>Quality</div>
            <div className={styles.RatingNumber}>{data.quality}</div>
          </div>
          <div className={styles.Difficulty}>
            <div className={styles.RatingHeader}>Difficulty</div>
            <div className={styles.RatingNumber}>{data.difficulty}</div>
          </div>
        </div>

        <div className={styles.main}>
          <div className={styles.Options}>
            <div className={styles.option}>
              <div className={styles.optionheader}>For credit:</div>
              {data.forCredit && <div className={styles.optionval}>Yes</div>}
              {!data.forCredit && <div className={styles.optionval}>No</div>}
            </div>
            <div className={styles.option}>
              <div className={styles.optionheader}>Attendance:</div>
              {data.attendance && <div className={styles.optionval}>Yes</div>}
              {!data.attendance && <div className={styles.optionval}>No</div>}
            </div>
            <div className={styles.option}>
              <div className={styles.optionheader}>Grade:</div>
              <div className={styles.optiontext}>{data.grade}</div>
            </div>
            <div className={styles.option}>
              <div className={styles.optionheader}>Textbook:</div>
              {data.textbook && <div className={styles.optionval}>Yes</div>}
              {!data.textbook && <div className={styles.optionval}>No</div>}
            </div>
            <div className={styles.date}>{data.date}</div>
          </div>

          <div className={styles.content}>{data.comment}</div>
        </div>
      </div>
    </div>
  );
}

export default Review;
