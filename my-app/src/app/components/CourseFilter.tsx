import React from "react";
import styles from "./css/courseFilter.module.css";

interface CourseFilterProps {
  filterTitle: string;
  options: { label: string }[];
  selectedFilters: { [key: string]: boolean };
  setSelectedFilters: (
    updateFunction: (prevState: { [key: string]: boolean }) => {
      [key: string]: boolean;
    }
  ) => void;
}

const CourseFilter: React.FC<CourseFilterProps> = ({
  filterTitle,
  options,
  selectedFilters,
  setSelectedFilters,
}) => {
  const handleCheckboxChange = (event: {
    target: { id: any; checked: any };
  }) => {
    const { id, checked } = event.target;
    setSelectedFilters((prevState) => ({
      ...prevState,
      [id]: checked,
    }));
  };

  return (
    <div>
      <h1 className={styles.filterTitle}> {filterTitle}</h1>
      <ul className={styles.listContainer}>
        {options.map((option) => (
          <li key={option.label}>
            <label className={styles.labelContainer} lang="en">
              <input
                type="checkbox"
                className={styles.filterOption}
                id={`${filterTitle}-${option.label}`}
                onChange={handleCheckboxChange}
                checked={
                  selectedFilters[`${filterTitle}-${option.label}`] || false
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
