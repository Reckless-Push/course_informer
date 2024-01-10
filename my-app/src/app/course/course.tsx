import CourseDesc from "@/app/components/CourseDesc";
import styles from "@/app/course/course.module.css";
import Reviews from "@/app/components/Reviews";
import { Course } from "@/types/course";
import { ComponentStates } from "@/types/ComponentStates";
import React, { useState } from "react";
import { Tabs, Tab, Box } from "@mui/material";

interface CoursePageProps {
  course_data: Course;
  onUserInputChange: any;
}

function CoursePage({
  course_data,
  onUserInputChange,
  onToggleComponent,
}: CoursePageProps & {
  onToggleComponent: (component: keyof ComponentStates) => void;
}) {
  console.log("Which course:", course_data);

  const [selectedTab, setSelectedTab] = useState(0); // Default to 'All Professors'
  const professors = course_data.instructors; // Assuming this is your list of professors

  const handleTabChange = (
    _: React.ChangeEvent<{}>,
    newValue: React.SetStateAction<number>
  ) => {
    setSelectedTab(newValue);
  };

  const handleClick = () => {
    onUserInputChange(course_data);
  };

  const handleBackClick = () => {
    onToggleComponent("courses");
    onToggleComponent("courseDashboard");
  };

  return (
    <div className={styles.CoursePage}>
      <button className={styles.backButton} onClick={handleBackClick}>
        Back
      </button>
      <div className={styles.main}>
        {course_data && <CourseDesc {...course_data}></CourseDesc>}
        <div className={styles.RateThisCourse}>
          <button
            className={styles.RateThisCourseBtn}
            onClick={() => handleClick()}
          >
            Add review
          </button>
        </div>
        <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
          <Tabs
            value={selectedTab}
            onChange={handleTabChange}
            aria-label="course tabs"
          >
            <Tab label="All Professors" />
            {professors.map((professor) => (
              <Tab
                label={professor.firstName + " " + professor.lastName}
                key={professor.id}
              />
            ))}
          </Tabs>
        </Box>
        <Reviews
          courseId={course_data.id}
          instructorId={
            selectedTab > 0 ? professors[selectedTab - 1].id : undefined
          }
        />
      </div>
    </div>
  );
}

export default CoursePage;
