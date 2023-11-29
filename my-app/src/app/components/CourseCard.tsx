import React, {ChangeEvent, FC, InputHTMLAttributes, useState} from 'react'
import styles from "./css/courseCard.module.css";

interface CourseCardProps {
  csID: string
  title: string;
  semester: string[];
}

const CourseCard: React.FC<CourseCardProps> = ({ csID, title, semester }) => {
  return (
    <div className={styles.card}>
      <div className={styles.left_content}>
        <h2 className={styles.course_title}>{csID}: {title}</h2>
        <p>Semesters offered: {semester && Array.isArray(semester) ? semester.join(', ') : 'Not available'}</p>
      </div>
      <div className={styles.right_content}>
        <span className={styles.star}>&#9733;</span>
        <button className={styles.next_button}>&#10132;</button>
      </div>
    </div>
  );
};

export default CourseCard;
