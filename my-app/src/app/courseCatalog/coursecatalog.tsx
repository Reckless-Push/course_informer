import React from "react";
import "./courseCatalog.css";
import CourseList from "../components/CourseList";
import CourseFiltersList from "../components/CourseFiltersList";
import { ComponentStates } from "@/types/ComponentStates";
import useFetchData from "@/app/hooks/useFetchData";
import { Course, CourseResponse } from "@/types/course";

interface CourseCatalogPage {
  onToggleComponent: (component: keyof ComponentStates) => void;
  onHome: () => void;
  componentStates: {
    courses: boolean;
    reviews: boolean;
    courseDashboard: boolean;
    login: boolean;
    user: boolean;
  };
}

function CourseCatalogPage({
  onToggleComponent,
  onHome,
  componentStates,
}: CourseCatalogPage) {
  const {
    data: courseData,
    loading: courseLoading,
    error: courseError
  } = useFetchData<CourseResponse>('https://localhost:8443/course');

  // Loading state
  if (courseLoading) {
    return <div>Loading...</div>;
  }

  // Error state
  if (courseError) {
    return <div>Error: {courseError.message}</div>;
  }

  return (
    <div className="container">
      <div className="filters_column">
        <CourseFiltersList />
      </div>
      <div className="list_column">
        <CourseList
          courseData={courseData}
          onToggleComponent={onToggleComponent}
          onHome={onHome}
          componentStates={componentStates}
        />
      </div>
    </div>
  );
}

export default CourseCatalogPage;
