import React from 'react'
import styles from "./css/courseFilter.module.css";

const CourseFilter = () => {
  return (
    <div >
        <h1 className={styles.filterTitle}> Term Offered:</h1>
        <ul className={styles.listContainer}>
            <li>
                <label>
                    <input type="checkbox" className={styles.filterOption} />
                    Spring 2024
                </label>
            </li>
            <li>
                <label>
                    <input type="checkbox" className={styles.filterOption} />
                    Fall 2023
                </label>
            </li>
            <li>
                <label>
                    <input type="checkbox" className={styles.filterOption}/>
                    Spring 2023
                </label>
            </li>
            <li>
                <label>
                    <input type="checkbox" className={styles.filterOption}/>
                    Fall 2022
                </label>
            </li>
        </ul>

    </div>
  )
}

export default CourseFilter;
