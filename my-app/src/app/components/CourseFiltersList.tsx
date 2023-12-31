import React from "react";
import CourseFilter from "./CourseFilter";
import SearchBox from "./SearchBox";
import useFetchData from "@/app/hooks/useFetchData";
import styles from "@/app/components/css/courseFilter.module.css";
import { SemesterResponse } from "@/types/semester";
import { CreditResponse } from "@/types/course";

interface CourseFilterListProps {
  selectedFilters: { [key: string]: boolean };
  setSelectedFilters: (
    updateFunction: (prevState: { [key: string]: boolean }) => {
      [key: string]: boolean;
    }
  ) => void;
}

const CourseFiltersList = ({
  selectedFilters,
  setSelectedFilters,
}: CourseFilterListProps) => {
  const termFilterTitle = "Term Offered:";
  const {
    data: semesterData,
    loading: semesterLoading,
    error: semesterError,
  } = useFetchData<SemesterResponse>(
    process.env.NEXT_PUBLIC_BASE_URL + "/course/semester"
  );

  const termOptions =
    semesterData?.semester_table.map((semester) => {
      return { label: semester.season + " " + semester.year };
    }) || [];

  const courseLevelFilterTitle = "Course Level:";
  const courseLevelOptions = [
    { label: "Undergraduate" },
    { label: "Graduate" },
  ];
  const {
    data: creditData,
    loading: creditLoading,
    error: creditError,
  } = useFetchData<CreditResponse>(
    process.env.NEXT_PUBLIC_BASE_URL + "/course/credit"
  );
  const creditFilterTitle = "Number of Credits:";

  // Loading state
  if (semesterLoading || creditLoading) {
    return <div>Loading...</div>;
  }

  // Error state
  if (semesterError || creditError) {
    return <div>Error: {semesterError?.message || creditError?.message}</div>;
  }

  const creditOptions =
    creditData?.credit_table.map((credit) => {
      return { label: credit + " credits" };
    }) || [];

  return (
    <div className={styles.sticky}>
      <SearchBox />
      <CourseFilter
        filterTitle={termFilterTitle}
        options={termOptions}
        selectedFilters={selectedFilters}
        setSelectedFilters={setSelectedFilters}
      />
      <CourseFilter
        filterTitle={courseLevelFilterTitle}
        options={courseLevelOptions}
        selectedFilters={selectedFilters}
        setSelectedFilters={setSelectedFilters}
      />
      <CourseFilter
        filterTitle={creditFilterTitle}
        options={creditOptions}
        selectedFilters={selectedFilters}
        setSelectedFilters={setSelectedFilters}
      />
    </div>
  );
};

export default CourseFiltersList;
