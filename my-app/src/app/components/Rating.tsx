"use client";
import styles from "@/app/components/css/courserating.module.css";
import { ComponentStates } from '@/types/ComponentStates'
import { Course } from "@/types/course";
import { Review, ReviewResponse } from "@/types/review";
import { stringify } from "querystring";




interface RatingProps {
    onToggleComponent: (component: keyof ComponentStates) => void
    onHome: () => void
    componentStates: {
        courses: boolean
        reviews: boolean
    }
    quality: number[]
    difficulty:number[]
    cics_id:number,cics_name:string

}
function handleClick({onToggleComponent,onHome,componentStates}: RatingProps) {
    onToggleComponent('reviews');
    onToggleComponent('courses');
}

function Rating({onToggleComponent,onHome,componentStates, quality,difficulty ,cics_id,cics_name}: RatingProps) {
    let qualitysum=quality.reduce((a, b) => a + b)/quality.length;
    let difficultysum=difficulty.reduce((a, b) => a + b)/difficulty.length;
    let qualitycount=Array(5).fill(0);
    quality.forEach((value) => {
        qualitycount[5-value]++;

    });
    let  perc=Array(5).fill('');
    qualitycount.forEach((value,index)=>{
        perc[index]="`"+String(value*100/quality.length)+"%`";
    })
    return (
        <div className={styles.Rating}>
            <div className={styles.RatingDistributionContainer}>
                <div className={styles.RatingDistributionHeader}>Rating Distribution<br /></div>
                <div className={styles.row}>
                {qualitycount.map((value,index) => (
                <div key={value}>
                    <div className={styles.left}>{5-index}star </div>
                    <div className={styles.middle}>
                        <div className={styles.barcontainer}>   
                        {/* TODO   fix bar graphs width */}
                            <div style={{width: perc[index]}}></div>
                        </div>
                    </div>
                    <div className={styles.right}>
                        <div>{value}</div>
                    </div>
                    </div>))}
                </div >
            </div>
            
            <div className={styles.RatingNumbers}>
                <div className={styles.RatingNumbersBox}>
                    <div className={styles.Quality}>
                        <div className={styles.QualityHeader}>Quality</div>
                        <div className={styles.QualityNumber}>{qualitysum}</div>
                    </div>
                    <div className={styles.Difficulty}>
                        <div className={styles.DifficultyHeader}>Difficulty</div>
                        <div className={styles.DifficultyNumber}>{difficultysum}</div>
                    </div>
                </div>
                <div className={styles.RateThisCourse}>
                    <button className={styles.RateThisCourseBtn}
                     onClick={() => handleClick({onToggleComponent,onHome,componentStates,quality,difficulty,cics_id,cics_name})}>
                    Add review
                    </button>
                </div >
            </div >
        </div >
    );
}

export default Rating;