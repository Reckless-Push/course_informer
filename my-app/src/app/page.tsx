"use client";
import React, { useEffect, useState } from "react";
import CoursePage from "@/app/course/course";
import { ComponentStates } from "@/types/ComponentStates";
import Navbar from "@/app/components/Navbar";
import ResponseForm from "@/app/review/review";
import CourseCatalogPage from "./courseCatalog/coursecatalog";

import useFetchData from "./hooks/useFetchData";
import { Course, CourseResponse } from "@/types/course";
import ProfilePage from "@/app/user/user";
import { courseData } from "../courseData";
type ComponentKey =
  | "courses"
  | "reviews"
  | "user"
  | "courseDashboard"
  | "login";

const Home = () => {
  const [componentStates, setComponentStates] = useState<ComponentStates>({
    courses: false,
    reviews: false,
    user: false,
    courseDashboard: true,
    login: false,
  });

  const onToggleComponent = (component: ComponentKey) => {
    setComponentStates((prev) => ({ ...prev, [component]: !prev[component] }));
  };

  const onHome = () => {
    setComponentStates({
      courses: false,
      reviews: false,
      user: false,
      courseDashboard: false,
      login: false,
    });
  };
  const {
    data: courseData,
    loading: courseLoading,
    error: courseError,
  } = useFetchData<CourseResponse>("https://localhost:8443/course");

  const [course, setcourse] = useState<Course>();
  const handleUserInputChange = (event: Course) => {
    setcourse(event);
    // Trigger the onToggleComponent function with the specified component
    componentStates.reviews = true;
    componentStates.courses = false;
    console.log("Component States:", componentStates);

    // setcourse(event.currentTarget.value);
  };
  // useEffect(() => {
  //   if (componentStates) {
  //     console.log(componentStates);
  //   }
  // }, [componentStates]);

  useEffect(() => {
    //Runs on every render
  });
  return (
    <div>
      <Navbar
        onToggleComponent={onToggleComponent}
        onHome={onHome}
        componentStates={componentStates}
      />

      {/* <Navbar></Navbar> */}
      {componentStates.courses && courseData && (
        <CoursePage
          onToggleComponent={onToggleComponent}
          onHome={onHome}
          componentStates={componentStates}
          courseData={courseData.course_table[1]}
          onUserInputChange={handleUserInputChange}
        />
      )}

      {componentStates.reviews && course && (
        // <div>{course.cicsId}</div>
        <ResponseForm
          onToggleComponent={onToggleComponent}
          onHome={onHome}
          componentStates={componentStates}
          onUserInputChange={course}
        />
      )}
      {componentStates.user && (
        <ProfilePage
          onToggleComponent={onToggleComponent}
          onHome={onHome}
          componentStates={componentStates}
        />
      )}
      {componentStates.courseDashboard && (
        <CourseCatalogPage
          onToggleComponent={onToggleComponent}
          onHome={onHome}
          componentStates={componentStates}
        />
      )}
    </div>
  );
};

export default Home;
