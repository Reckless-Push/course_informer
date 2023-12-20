import React from "react";
import CourseList from "../components/CourseList";
import CourseFiltersList from "../components/CourseFiltersList";
import {ComponentStates} from "@/types/ComponentStates";
import useFetchData from "@/app/hooks/useFetchData";
import {CourseResponse} from "@/types/course";
import styles from "@/app/courseCatalog/courseCatalog.module.css";

interface CourseCatalogPage {
  onUserInputChange: any;
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
  onUserInputChange,
  onToggleComponent,
  onHome,
  componentStates,
}: CourseCatalogPage) {
  const {
    data: courseData,
    loading: courseLoading,
    error: courseError
  } = useFetchData<CourseResponse>(process.env.NEXT_PUBLIC_BASE_URL + '/course');

  // Loading state
  if (courseLoading) {
    return <div>Loading...</div>;
  }

  // Error state
  if (courseError) {
    return <div>Error: {courseError.message}</div>;
  }

  return (
    <div className={styles.container}>
      <div className={styles.filters_column}>
        <CourseFiltersList />
      </div>
      <div className={styles.list_column}>
        <CourseList
          onUserInputChange = {onUserInputChange}
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
