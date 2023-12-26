import React, { useState } from "react";
import styles from "./css/courseFilter.module.css";

interface CourseFilterProps {
  filterTitle: string;
  options: { label: string }[];
}

const CourseFilter: React.FC<CourseFilterProps> = (props) => {
  const [selectedOptions, setSelectedOptions] = useState<{
    [key: string]: boolean;
  }>({});

  const handleCheckboxChange = (event: {
    target: { id: any; checked: any };
  }) => {
    setSelectedOptions({
      ...selectedOptions,
      [event.target.id]: event.target.checked,
    });
  };

  return (
    <div>
      <h1 className={styles.filterTitle}> {props.filterTitle}</h1>
      <ul className={styles.listContainer}>
        {props.options.map((option, index) => (
          <li key={index}>
            <label className={styles.labelContainer} lang="en">
              <input
                type="checkbox"
                className={styles.filterOption}
                id={`${props.filterTitle}-${index}`}
                onChange={handleCheckboxChange}
                checked={
                  selectedOptions[`${props.filterTitle}-${index}`]
                    ? selectedOptions[`${props.filterTitle}-${index}`]
                    : false
                }
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
