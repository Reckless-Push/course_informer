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
    onHome();
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
    onToggleComponent("reviews");
    console.log("Component States:", componentStates);
  };

  useEffect(() => {
    //Runs on the first render
    //And any time any dependency value changes
  }, [componentStates, setComponentStates]);
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
          course_data={courseData.course_table[0]}
          onUserInputChange={handleUserInputChange}
        />
      )}

      {componentStates.reviews && course && <ResponseForm {...course} />}
      {componentStates.user && <ProfilePage />}
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
