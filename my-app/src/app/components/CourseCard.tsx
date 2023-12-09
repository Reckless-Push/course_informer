import React, { ChangeEvent, FC, InputHTMLAttributes, useState } from "react";
import styles from "./css/courseCard.module.css";
import { ComponentStates } from "@/types/ComponentStates";
import { Course } from "@/types/course";

interface CourseCardProps {
  course: Course
  onToggleComponent: (component: keyof ComponentStates) => void;
  onHome: () => void;
  componentStates: {
    courses: boolean;
    reviews: boolean;
    courseDashboard: boolean;
    login: boolean;
  };
}

function handleClick(props: CourseCardProps) {
  //props.onToggleComponent('reviews');
  props.onToggleComponent("courses");
  //props.onToggleComponent('login');
}

const CourseCard: React.FC<CourseCardProps> = (props) => {
  const formattedSemesters = props.course.semestersOffered
    ? props.course.semestersOffered.map(semester => `${semester.season} ${semester.year}`).join(', ')
    : 'Not available';return (
    <div className={styles.card}>
      <div className={styles.left_content}>
        <h2 className={styles.course_title}>
          {props.course.cicsId}: {props.course.name}
        </h2>
        <p>
          Semesters offered:{" "}
          {formattedSemesters}
        </p>
      </div>
      <div className={styles.right_content}>
        <span className={styles.star}>&#9733;</span>
        <button
          className={styles.next_button}
          onClick={() => handleClick(props)}
        >
          &#10132;
        </button>
      </div>
    </div>
  );
};

export default CourseCard;
