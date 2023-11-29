"use client";
import React, { Component } from "react";
import styles from "@/app/components/css/courserating.module.css";
import Link from 'next/link';





class Rating extends Component {


    render() {

        // const router = useRouter()

        return (
            <div className={styles.Rating}>
                <div className={styles.RatingDistributionContainer}>
                    <div className={styles.RatingDistributionHeader}>Rating Distribution<br /></div>

                    <div className={styles.row}>
                        <div className={styles.left}>5 star </div>
                        <div className={styles.middle}>
                            <div className={styles.barcontainer}>
                                <div className={styles.bar5}></div>
                            </div>
                        </div>
                        <div className={styles.right}>
                            <div>150</div>
                        </div>
                        <div className={styles.left}>
                            <div>4 star</div>
                        </div>
                        <div className={styles.middle}>
                            <div className={styles.barcontainer}>
                                <div className={styles.bar4}></div>
                            </div>
                        </div>
                        <div className={styles.right}>
                            <div>63</div>
                        </div>
                        <div className={styles.left}>
                            <div>3 star</div>
                        </div>
                        <div className={styles.middle}>
                            <div className={styles.barcontainer}>
                                <div className={styles.bar3}></div>
                            </div>
                        </div>
                        <div className={styles.right}>
                            <div>15</div>
                        </div>
                        <div className={styles.left}>
                            <div>2 star</div>
                        </div>
                        <div className={styles.middle}>
                            <div className={styles.barcontainer}>
                                <div className={styles.bar2}></div>
                            </div>
                        </div>
                        <div className={styles.right}>
                            <div>6</div>
                        </div>
                        <div className={styles.left}>
                            <div>1 star</div>
                        </div>
                        <div className={styles.middle}>
                            <div className={styles.barcontainer}>
                                <div className={styles.bar1}></div>
                            </div>
                        </div>
                        <div className={styles.right}>
                            <div></div>
                        </div>
                    </div >
                </div>
                <div className={styles.RatingNumbers}>
                    <div className={styles.RatingNumbersBox}>
                        <div className={styles.Quality}>
                            <div className={styles.QualityHeader}>Quality</div>
                            <div className={styles.QualityNumber}>4.0</div>
                        </div>
                        <div className={styles.Difficulty}>
                            <div className={styles.DifficultyHeader}>Difficulty</div>
                            <div className={styles.DifficultyNumber}>3.0</div>
                        </div>
                    </div>
                    <div className={styles.RateThisCourse}><button className={styles.RateThisCourseBtn}><Link href="/login">
                        myroute
                    </Link>
                        Rate this Course</button></div>
                </div>
            </div >
        );
    }
}

export default Rating;