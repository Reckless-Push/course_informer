import React from "react";
import styles from "./css/courseCard.module.css";
import { ComponentStates } from "@/types/ComponentStates";
import { Course } from "@/types/course";
import axios from "axios";

interface CourseCardProps {
  onUserInputChange: any;
  course: Course;
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
  props.onUserInputChange(props.course);
  props.onToggleComponent("courses");
  console.log(props.course.cicsId);
  //props.onToggleComponent('login');
}

function handleStarClick(props: CourseCardProps) {
  axios
    .post<String>(
      process.env.NEXT_PUBLIC_BASE_URL + "/user/current/star",
      props.course
    )
    .then(console.log)
    .catch(console.error);
  console.log("Star clicked");
}

const CourseCard: React.FC<CourseCardProps> = (props) => {
  const formattedSemesters = props.course.semestersOffered
    ? props.course.semestersOffered
        .map((semester) => `${semester.season} ${semester.year}`)
        .join(", ")
    : "Not available";
  return (
    <div className={styles.card}>
      <div className={styles.left_content}>
        <h2 className={styles.course_title}>
          {props.course.department} {props.course.cicsId}: {props.course.name}
        </h2>
        <p>Semesters offered: {formattedSemesters}</p>
      </div>
      <div className={styles.right_content}>
        <button className={styles.star} onClick={() => handleStarClick(props)}>
          &#9733;
        </button>
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
