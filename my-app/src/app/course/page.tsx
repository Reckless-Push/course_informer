"use client";
import React, { Component } from "react";
import Navbar from "@/app/components/Navbar";
import CourseDesc from "@/app/components/CourseDesc";
import Rating from "@/app/components/Rating";
import Review from "@/app/components/Review";
import styles from "@/app/course/course.module.css";

class CoursePage extends Component {
    render() {
        return (
            <div className={styles.CoursePage}>
                <Navbar></Navbar>
                <div className={styles.main}>
                    <CourseDesc></CourseDesc>
                    <Rating></Rating>

                    <Review></Review>
                    <Review></Review>
                    <Review></Review>
                    <Review></Review>

                </div>

            </div >
        );
    }
}
export default CoursePage;