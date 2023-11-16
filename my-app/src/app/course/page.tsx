"use client";
import React, { Component } from "react";
import Navbar from "../components/Navbar";
import CourseDesc from "../components/CourseDesc";
import Rating from "../components/Rating";
import styles from "./course.module.css";


class CoursePage extends Component {
    render() {
        return (
            <div className={styles.CoursePage}>
                <Navbar></Navbar>
                <div className={styles.main}>
                    <CourseDesc></CourseDesc>
                    <Rating></Rating>

                </div>

            </div >
        );
    }
}
export default CoursePage;