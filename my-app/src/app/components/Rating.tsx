"use client";
import styles from "@/app/components/css/courserating.module.css";

interface RatingProps {
  quality: number[];
  difficulty: number[];
}

function Rating({ quality, difficulty }: RatingProps) {
  let qualitySum = (quality.reduce((a, b) => a + b) / quality.length).toFixed(
    1
  );
  let difficultySum = (
    difficulty.reduce((a, b) => a + b) / difficulty.length
  ).toFixed(1);
  let qualityCount = Array(5).fill(0);
  quality.forEach((value) => {
    qualityCount[5 - value]++;
  });
  let percent = Array(5).fill("");
  qualityCount.forEach((value, index) => {
    percent[index] = String((value * 100) / quality.length) + "%";
  });
  return (
    <div className={styles.Rating}>
      <div className={styles.RatingDistributionContainer}>
        <div className={styles.RatingDistributionHeader}>
          Rating Distribution
          <br />
        </div>
        <div className={styles.row}>
          {qualityCount.map((value, index) => (
            <div key={value}>
              <div className={styles.left}>{5 - index}star</div>
              <div className={styles.middle}>
                <div className={styles.barContainer}>
                  <div style={{ width: percent[index] }}></div>
                </div>
              </div>
              <div className={styles.right}>
                <div>{value}</div>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className={styles.RatingNumbers}>
        <div className={styles.RatingNumbersBox}>
          <div className={styles.Quality}>
            <div className={styles.QualityHeader}>Quality</div>
            <div className={styles.QualityNumber}>{qualitySum}</div>
          </div>
          <div className={styles.Difficulty}>
            <div className={styles.DifficultyHeader}>Difficulty</div>
            <div className={styles.DifficultyNumber}>{difficultySum}</div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Rating;
