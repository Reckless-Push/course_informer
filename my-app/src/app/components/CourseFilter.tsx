import React from "react";
import styles from "./css/courseFilter.module.css";

interface CourseFilterProps {
  filterTitle: string;
  options: { label: string }[];
}

const CourseFilter: React.FC<CourseFilterProps> = (props) => {
  return (
    <div>
      <h1 className={styles.filterTitle}> {props.filterTitle}</h1>
      <ul className={styles.listContainer}>
        {props.options.map((option, index) => (
          <li key={index}>
            <label>
              <input
                type="checkbox"
                className={styles.filterOption}
                id={`${props.filterTitle}-${index}`}
              />
              {option.label}
            </label>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default CourseFilter;
