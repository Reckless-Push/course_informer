"use client";

import React, { Component } from "react";
import styles from "./review.module.css";
import Navbar from "../components/Navbar";

class ResponseForm extends Component {
    render() {
        return (
            <div>
                <Navbar></Navbar>
                <div className={styles.main}>
                    <div className={styles.content}>
                        <div className={styles.CourseName}>COMPSCI 520 Thry + Practice; Software Engin</div>
                        <form className={styles.FormContainer}>
                            <div className={styles.SelectProfessor}>
                                <label className={styles.Text}>Select Professor</label>
                                <select id="Professor">
                                    <option value="null"></option>
                                    <option value="HeatherConboy">Heather Conboy</option>
                                    <option value="JuanZhai">Juan Zhai</option>
                                </select>
                            </div>
                            <div className={styles.SelectProfessor}>
                                <label className={styles.Text}>Rate your Professor</label>
                                <input type="range" min="1" max="5" step="1" defaultValue="4" className={styles.slider} id="myRange"></input>
                            </div>
                            <div className={styles.SelectProfessor}>
                                <label className={styles.Text}>How difficult was this Professor</label>
                                <input type="range" min="1" max="5" step="1" defaultValue="4" className={styles.slider} id="myRange"></input>
                            </div>
                            <div className={styles.RetakeCourse}>
                                <label className={styles.Text}>Would you retake this Course?</label>
                                <label className={styles.retake}>
                                    <input type="radio" id={styles.RetakeCourseRadio} value="Yes" name="retake"></input>
                                    <span className={styles.checkmark}></span>Yes
                                </label>
                                <label className={styles.retake}>
                                    <input type="radio" id={styles.RetakeCourseRadio} value="No" name="retake"></input>
                                    <span className={styles.checkmark}></span> No
                                </label>
                            </div>
                            <div className={styles.SelectProfessor}>
                                <label className={styles.Text}>Was this course taken for credit</label>
                                <label className={styles.retake}>
                                    <input type="radio" id={styles.RetakeCourseRadio} value="Yes" name="credit"></input>
                                    <span className={styles.checkmark}></span>Yes
                                </label>
                                <label className={styles.retake}>
                                    <input type="radio" id={styles.RetakeCourseRadio} value="No" name="credit"></input>
                                    <span className={styles.checkmark}></span> No
                                </label>
                            </div>
                            <div className={styles.SelectProfessor}>
                                <label className={styles.Text}>Did this professor use textbooks</label>
                                <label className={styles.retake}>
                                    <input type="radio" id={styles.RetakeCourseRadio} value="Yes" name="textbook"></input>
                                    <span className={styles.checkmark}></span>Yes
                                </label>
                                <label className={styles.retake}>
                                    <input type="radio" id={styles.RetakeCourseRadio} value="No" name="textbook"></input>
                                    <span className={styles.checkmark}></span> No
                                </label>
                            </div>
                            <div className={styles.SelectProfessor}>
                                <label className={styles.Text}>Was attendance mandatory?</label>
                                <label className={styles.retake}>
                                    <input type="radio" id={styles.RetakeCourseRadio} value="Yes" name="attendance"></input>
                                    <span className={styles.checkmark}></span>Yes
                                </label>
                                <label className={styles.retake}>
                                    <input type="radio" id={styles.RetakeCourseRadio} value="No" name="attendance"></input>
                                    <span className={styles.checkmark}></span> No
                                </label>
                            </div>
                            <div className={styles.SelectProfessor}>
                                <label className={styles.Text}>Select grade</label>
                                {/* <input list="browsers" name="browser" /> */}
                                <select id="grade">
                                    <option value="null"></option>
                                    <option value="A">A</option>
                                    <option value="A-">A-</option>
                                    <option value="B">B</option>
                                    <option value="B-">B-</option>
                                    <option value="C">C</option>
                                </select>
                            </div>
                            <div className={styles.addComments}>
                                <label className={styles.Text} >Additional comments</label>
                                <textarea className={styles.addCommentstext} placeholder="Add more feedback">
                                </textarea>
                            </div>
                            <div className={styles.RateThisCourse}>
                                <button className={styles.RateThisCourseBtn}>Rate this Course</button>
                            </div>
                        </form>
                    </div>
                </div >

            </div >

        );
    }
}

export default ResponseForm;