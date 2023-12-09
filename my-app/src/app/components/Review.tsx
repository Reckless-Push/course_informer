"use client";
import styles from "@/app/components/css/coursereview.module.css";
import { Review } from "@/types/review";

function Review(data: Review) {
  let formatteddate = "";
  if (data.date) {
    const date = new Date(data.date);
    formatteddate =
      date.getUTCDate() +
      " " +
      date.toLocaleString("default", { month: "short" }) +
      ", " +
      date.getUTCFullYear();
    console.log(formatteddate);
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
              <div className={styles.optionheader}>For credit:</div>
              {data.forCredit ? (
                <div className={styles.optionval}>Yes</div>
              ) : (
                <div className={styles.optionval}>No</div>
              )}
            </div>
            <div className={styles.option}>
              <div className={styles.optionheader}>Attendance:</div>
              {data.attendance ? (
                <div className={styles.optionval}>Yes</div>
              ) : (
                data.attendance && <div className={styles.optionval}>No</div>
              )}
            </div>
            <div className={styles.option}>
              <div className={styles.optionheader}>Grade:</div>
              <div className={styles.optionval}>{data.grade}</div>
            </div>
            <div className={styles.option}>
              <div className={styles.optionheader}>Textbook:</div>
              {data.textbook ? (
                <div className={styles.optionval}>Yes</div>
              ) : (
                <div className={styles.optionval}>No</div>
              )}
            </div>
            <div className={styles.date}>{formatteddate}</div>
          </div>
          <div className={styles.prof}>
            <div className={styles.profheader}>Professor: </div>
            <div className={styles.profval}>
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
