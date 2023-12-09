import React from "react";
import CourseCard from "./CourseCard";
import { ComponentStates } from "@/types/ComponentStates";
import { CourseResponse } from "@/types/course";

interface CourseListProps {
  courseData: CourseResponse | null
  onToggleComponent: (component: keyof ComponentStates) => void;
  onHome: () => void;
  componentStates: {
    courses: boolean;
    reviews: boolean;
    courseDashboard: boolean;
    login: boolean;
  };
}

function CourseList({
  courseData,
  onToggleComponent,
  onHome,
  componentStates,
}: CourseListProps) {
  return (
    <div>
      {courseData?.course_table.map((course) => {
      // Transform semestersOffered array to an array of semester strings
      const semesters: string[] = course.semestersOffered.map((semesterInfo) => {
        const { season, year } = semesterInfo;
        return `${season} ${year}`;
      });

      return (
        <CourseCard
          key={course.cicsId}
          course={course}
          onToggleComponent={onToggleComponent}
          onHome={onHome}
          componentStates={componentStates}
        />
      );
    })}
    </div>
  );
}

export default CourseList;
