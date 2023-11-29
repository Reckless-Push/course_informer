"use client";
import React, { Component } from "react";
import styles from "@/app/components/css/coursedesc.module.css";

class CourseDesc extends Component {
    render() {
        return (
            <div className={styles.content}>
                {/* <button >Like</button>
                <button >Back</button> */}
                <div className={styles.CourseName}>COMPSCI 520 Thry + Practice; Software Engin</div>
                <div className={styles.Credits}>Credits: 3</div>
                <div className={styles.Termtotal} ><div className={styles.Term}>Term Offered: Spring 2024</div> <div className={styles.PrevTerm}>Previous Terms offered: Fall 2023, ...</div></div>
                <div className={styles.Proftotal} ><div className={styles.Prof}>INSTRUCTOR(S): Juan Zhai</div> <div className={styles.PrevProf}>Previous Instructors: Heather Conboy ...</div></div>
                <div className={styles.Prereqs}>Undergraduate Prerequisites: COMPSCI 320 (or COMPSCI 220 and COMPSCI 326). </div>
                <div className={styles.Desc}>Description:<br />Introduces students to the principal activities and state-of-the-art techniques involved in developing high-quality software systems. Topics include: requirements engineering, formal specification methods, design principles & patterns, verification & validation, debugging, and automated software engineering. This course counts as a CS Elective for the CS Major. Undergraduate Prerequisites: COMPSCI 320 (or COMPSCI 220 and COMPSCI 326). </div>
            </div>
        );
    }
}

export default CourseDesc;