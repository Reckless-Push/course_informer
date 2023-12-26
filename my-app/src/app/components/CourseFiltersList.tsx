import React from "react";
import CourseFilter from "./CourseFilter";
import SearchBox from "./SearchBox";

const CourseFiltersList = () => {
  const termFilterTitle = "Term Offered:";
  const termOptions = [
    { label: "Spring 2024" },
    { label: "Fall 2023" },
    { label: "Spring 2023" },
    { label: "Fall 2022" },
  ];
  const courseLevelFilterTitle = "Course Level:";
  const courseLevelOptions = [
    { label: "Undergraduate" },
    { label: "Graduate" },
  ];
  const creditsFilterTitle = "Number of Credits:";
  const creditsOptions = [{ label: "1-3 credits" }, { label: "4-6 credits" }];
  return (
    <div>
      <SearchBox />
      <CourseFilter filterTitle={termFilterTitle} options={termOptions} />
      <CourseFilter
        filterTitle={courseLevelFilterTitle}
        options={courseLevelOptions}
      />
      <CourseFilter filterTitle={creditsFilterTitle} options={creditsOptions} />
    </div>
  );
};

export default CourseFiltersList;
