import React, { useState } from "react";
import CourseList from "../components/CourseList";
import CourseFiltersList from "../components/CourseFiltersList";
import { ComponentStates } from "@/types/ComponentStates";
import { SelectedFilters } from "@/types/course";
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
  const [selectedFilters, setSelectedFilters] = useState<SelectedFilters>({});

  return (
    <div className={styles.container}>
      <div className={styles.filters_column}>
        <CourseFiltersList
          selectedFilters={selectedFilters}
          setSelectedFilters={setSelectedFilters}
        />
      </div>
      <div className={styles.list_column}>
        <CourseList
          selectedFilters={selectedFilters}
          onUserInputChange={onUserInputChange}
          onToggleComponent={onToggleComponent}
          onHome={onHome}
          componentStates={componentStates}
        />
      </div>
    </div>
  );
}

export default CourseCatalogPage;
