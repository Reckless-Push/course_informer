"use client";
import styles from "@/app/components/css/courserating.module.css";

interface RatingProps {
  quality: number[];
  difficulty: number[];
}

function Rating({ quality, difficulty }: RatingProps) {
  let qualitysum = (quality.reduce((a, b) => a + b) / quality.length).toFixed(
    1
  );
  let difficultysum = (
    difficulty.reduce((a, b) => a + b) / difficulty.length
  ).toFixed(1);
  let qualitycount = Array(5).fill(0);
  quality.forEach((value) => {
    qualitycount[5 - value]++;
  });
  let perc = Array(5).fill("");
  qualitycount.forEach((value, index) => {
    perc[index] = String((value * 100) / quality.length) + "%";
  });
  return (
    <div className={styles.Rating}>
      <div className={styles.RatingDistributionContainer}>
        <div className={styles.RatingDistributionHeader}>
          Rating Distribution
          <br />
        </div>
        <div className={styles.row}>
          {qualitycount.map((value, index) => (
            <div key={value}>
              <div className={styles.left}>{5 - index}star </div>
              <div className={styles.middle}>
                <div className={styles.barcontainer}>
                  <div style={{ width: perc[index] }}></div>
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
            <div className={styles.QualityNumber}>{qualitysum}</div>
          </div>
          <div className={styles.Difficulty}>
            <div className={styles.DifficultyHeader}>Difficulty</div>
            <div className={styles.DifficultyNumber}>{difficultysum}</div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Rating;
