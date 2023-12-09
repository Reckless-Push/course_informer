import React, { ChangeEvent, FC, InputHTMLAttributes, useState } from "react";
import styles from "./css/courseCard.module.css";
import { ComponentStates } from "@/types/ComponentStates";

interface CourseCardProps {
  csID: number;
  title: string;
  semester: string[];
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
  return (
    <div className={styles.card}>
      <div className={styles.left_content}>
        <h2 className={styles.course_title}>
          {props.csID}: {props.title}
        </h2>
        <p>
          Semesters offered:{" "}
          {props.semester && Array.isArray(props.semester)
            ? props.semester.join(", ")
            : "Not available"}
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
