"use client";
import React from "react";
import "./courseCatalog.css";
import CourseList from "../components/CourseList";
import CourseFiltersList from "../components/CourseFiltersList";

const courseCatalogPage = () => {
  return (
    <div className="container">
      <div className="filters_column">
        <CourseFiltersList />
      </div>
      <div className="list_column">
        <CourseList />
      </div>
    </div>
  );
};

export default courseCatalogPage;
