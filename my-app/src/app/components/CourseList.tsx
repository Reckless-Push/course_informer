import React from "react";
import CourseCard from "./CourseCard";
import { courseData } from "../../courseData";
import { ComponentStates } from "@/types/ComponentStates";

interface CourseListProps {
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
  onToggleComponent,
  onHome,
  componentStates,
}: CourseListProps) {
  return (
    <div>
      {courseData.map((course) => (
        <CourseCard
          key={course.title}
          csID={course.csID}
          title={course.title}
          semester={course.semester}
          onToggleComponent={onToggleComponent}
          onHome={onHome}
          componentStates={componentStates}
        />
      ))}
    </div>
  );
}

export default CourseList;
