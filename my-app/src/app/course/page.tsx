"use client";
import React, { Component } from "react";
import Navbar from "@/app/components/Navbar";
import CourseDesc from "@/app/components/CourseDesc";
import Rating from "@/app/components/Rating";
import Review from "@/app/components/Review";
import styles from "@/app/course/course.module.css";
import useFetchData from '@/app/hooks/useFetchData';
import { CourseResponse } from "@/types/course";

function CoursePage() {

    // const {
    //     data: courseData,
    //     loading: courseLoading,
    //     error: courseError
    // } = useFetchData<CourseResponse>('https://localhost:8443/course');

    // if (courseLoading) return <div>Loading...</div>;
    // if (courseError) return <div>Error:{courseError?.message}</div>;
    // console.log(courseData);
    return (
        <div className={styles.CoursePage}>
            {/* <Navbar></Navbar> */}
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
export default CoursePage;