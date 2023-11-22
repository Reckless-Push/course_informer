import React, {ChangeEvent, FC, InputHTMLAttributes, useState} from 'react'
import styles from "./css/courseCard.module.css";

const CourseCard = () => {
  return (
    <div className={styles.card}>
      <div className={styles.left_content}>
        <h2 className={styles.course_title}>CS 520: Software</h2>
        <p>Semesters offered: Spring 2024, Fall 2024</p>
      </div>
      <div className={styles.right_content}>
        <span className={styles.star}>&#9733;</span>
        <button className={styles.next_button}>&#10132;</button>
      </div>
    </div>
  );
};

export default CourseCard;
