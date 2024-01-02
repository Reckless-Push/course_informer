import React, { useEffect, useState } from "react";
import CourseCard from "./CourseCard";
import { ComponentStates } from "@/types/ComponentStates";
import usePostData from "@/app/hooks/usePostData";
import { CourseResponse, SelectedFilters } from "@/types/course";
import { CourseFilterData } from "@/types/CourseFilterData";
import axios from "axios";

interface CourseListProps {
  selectedFilters: SelectedFilters;
  onUserInputChange: any;
  onToggleComponent: (component: keyof ComponentStates) => void;
  onHome: () => void;
  componentStates: {
    courses: boolean;
    reviews: boolean;
    courseDashboard: boolean;
    login: boolean;
  };
}

const processSelectedFilters = (selectedFilters: {
  [key: string]: boolean;
}): CourseFilterData => {
  let processedFilters: CourseFilterData = {
    credits: [],
    undergrad: false,
    grad: false,
    semestersOffered: [],
  };

  console.log(selectedFilters);
  for (let key in selectedFilters) {
    if (selectedFilters[key]) {
      if (key.includes("Undergraduate")) {
        processedFilters.undergrad = true;
      } else if (key.includes("Graduate")) {
        processedFilters.grad = true;
      } else if (key.includes("credits")) {
        processedFilters.credits.push(parseInt(key.split("-")[1]));
      } else if (key.includes("Term Offered:-")) {
        processedFilters.semestersOffered.push({
          season: key.split("-")[1].split(" ")[0],
          year: parseInt(key.split(" ")[2]),
        });
      }
    }
  }
  console.log(processedFilters);
  return processedFilters;
};

function CourseList({
  onUserInputChange,
  onToggleComponent,
  onHome,
  componentStates,
  selectedFilters,
}: CourseListProps) {
  const [isSubmitClicked, setIsSubmitClicked] = useState(false);

  const {
    data: courseData,
    loading: courseLoading,
    error: courseError,
  } = usePostData<CourseResponse, CourseFilterData>(
    process.env.NEXT_PUBLIC_BASE_URL + "/course/filter",
    processSelectedFilters(selectedFilters),
    isSubmitClicked
  );

  const [favoriteCourses, setFavoriteCourses] = useState<number[]>([]);
  // Function to fetch favorite courses
  const fetchFavoriteCourses = () => {
    axios
      .get(process.env.NEXT_PUBLIC_BASE_URL + "/user/current/favorites")
      .then((response) => setFavoriteCourses(response.data))
      .catch((error) => console.error(error));
  };

  useEffect(() => {
    fetchFavoriteCourses();
  }, []);

  useEffect(() => {
    if (isSubmitClicked) {
      setIsSubmitClicked(false);
    }
  }, [isSubmitClicked]);

  useEffect(() => {
    setIsSubmitClicked(true);
  }, [selectedFilters]);

  // Loading state
  if (courseLoading) {
    return <div>Loading...</div>;
  }

  // Error state
  if (courseError) {
    return <div>Error: {courseError.message}</div>;
  }

  return (
    <div>
      {courseData?.course_table.map((course) => {
        // Transform semestersOffered array to an array of semester strings
        course.semestersOffered.map((semesterInfo) => {
          const { season, year } = semesterInfo;
          return `${season} ${year}`;
        });

        return (
          <CourseCard
            fetchFavoriteCourses={fetchFavoriteCourses}
            favoriteCourses={favoriteCourses}
            key={course.cicsId}
            onUserInputChange={onUserInputChange}
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
