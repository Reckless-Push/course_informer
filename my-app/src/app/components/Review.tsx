"use client";
import styles from "@/app/components/css/coursereview.module.css";
import { Review } from "@/types/review";

function Review(data: Review) {
  let formattedDate = "";
  if (data.date) {
    const date = new Date(data.date);
    formattedDate =
      date.getUTCDate() +
      " " +
      date.toLocaleString("default", { month: "short" }) +
      ", " +
      date.getUTCFullYear();
    console.log(formattedDate);
  }

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
              <div className={styles.optionHeader}>For credit:</div>
              {data.forCredit ? (
                <div className={styles.optionVal}>Yes</div>
              ) : (
                <div className={styles.optionVal}>No</div>
              )}
            </div>
            <div className={styles.option}>
              <div className={styles.optionHeader}>Attendance:</div>
              {data.attendance ? (
                <div className={styles.optionVal}>Yes</div>
              ) : (
                data.attendance && <div className={styles.optionVal}>No</div>
              )}
            </div>
            <div className={styles.option}>
              <div className={styles.optionHeader}>Grade:</div>
              <div className={styles.optionVal}>{data.grade}</div>
            </div>
            <div className={styles.option}>
              <div className={styles.optionHeader}>Textbook:</div>
              {data.textbook ? (
                <div className={styles.optionVal}>Yes</div>
              ) : (
                <div className={styles.optionVal}>No</div>
              )}
            </div>
            <div className={styles.date}>{formattedDate}</div>
          </div>
          <div className={styles.prof}>
            <div className={styles.profHeader}>Professor:</div>
            <div className={styles.profVal}>
              {data.professor?.firstName} {data.professor?.lastName}
            </div>
          </div>
          <div className={styles.content}>{data.comment}</div>
        </div>
      </div>
    </div>
  );
}

export default Review;
