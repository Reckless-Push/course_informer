"use client";
import CourseDesc from "@/app/components/CourseDesc";
import Rating from "@/app/components/Rating";
import Review from "@/app/components/Review";
import styles from "@/app/course/course.module.css";
import { ComponentStates } from '@/types/ComponentStates'
import useFetchData from "../hooks/useFetchData";
import { CourseResponse } from "@/types/course";
import { ReviewResponse } from "@/types/review";
interface CoursePageProps {
    onToggleComponent: (component: keyof ComponentStates) => void
    onHome: () => void
    componentStates: {
        courses: boolean
        reviews: boolean
    }
    cics_id:number
}

function CoursePage({onToggleComponent,onHome,componentStates,cics_id}: CoursePageProps) {
    //TODO needs to be replaced by course object
    const {
        data: courseData,
        loading: courseLoading,
        error: courseError
    } = useFetchData<CourseResponse>('https://localhost:8443/course');
    const {
        data: reviewData,
        loading: reviewLoading,
        error: reviewError
    } = useFetchData<ReviewResponse>('https://localhost:8443/review');

    if (courseLoading||reviewLoading) return <div>Loading...</div>;
    if (courseError||reviewError) return <div>Error:{courseError?.message}</div>;
    // 
    console.log(courseData?.course_table[0])
    const qualityscore=new Array();
    const difficultyscore=new Array();
    reviewData?.review_table.forEach((review) => {
    if (review.course.cicsId==cics_id){
        qualityscore.push(review.quality);
        difficultyscore.push(review.difficulty);
    }
});
    return (
        
        <div className={styles.CoursePage}>
            {/* <Navbar></Navbar> */}
            <div className={styles.main}>
                <CourseDesc></CourseDesc>
                <Rating onToggleComponent={onToggleComponent}
                    onHome={onHome}
                    componentStates={componentStates}
                    quality= {qualityscore}
                    difficulty={difficultyscore}/>

                {reviewData?.review_table.map(review => (
                <Review key={review.comment} quality={review.quality} difficulty={review.difficulty} comment={review.comment} date={review.date} />
            ))}

            </div>

        </div >
    );
}
export default CoursePage;