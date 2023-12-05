"use client";
import CourseDesc from "@/app/components/CourseDesc";
import Rating from "@/app/components/Rating";
import Review from "@/app/components/Review";
import styles from "@/app/course/course.module.css";
import { ComponentStates } from '@/types/ComponentStates'
import useFetchData from "../hooks/useFetchData";
import { ReviewResponse } from "@/types/review";
import { Course } from "@/types/course";
interface CoursePageProps {
    onToggleComponent: (component: keyof ComponentStates) => void
    onHome: () => void
    componentStates: {
        courses: boolean
        reviews: boolean
    }
    courseData: Course
}

function CoursePage({ onToggleComponent, onHome, componentStates, courseData }: CoursePageProps) {
    const {
        data: reviewData,
        loading: reviewLoading,
        error: reviewError
    } = useFetchData<ReviewResponse>('https://localhost:8443/review');

    if (reviewLoading) return <div>Loading...</div>;
    if (reviewError) return <div>Error:{reviewError?.message}</div>;

    const qualityscore = new Array();
    const difficultyscore = new Array();
    reviewData?.review_table.forEach((review) => {
        if (review.course && review.course.cicsId == courseData.cicsId) {
            qualityscore.push(review.quality);
            difficultyscore.push(review.difficulty);
        }
    });
    return (

        <div className={styles.CoursePage}>
            {/* <Navbar></Navbar> */}
            <div className={styles.main}>
                {courseData && <CourseDesc {...courseData}></CourseDesc>}

                <Rating onToggleComponent={onToggleComponent}
                    onHome={onHome}
                    componentStates={componentStates}
                    quality={qualityscore}
                    difficulty={difficultyscore}
                    cics_id={courseData.cicsId}
                    cics_name={courseData.name} />

                {reviewData?.review_table.map(review => (
                    <Review key={review.comment} {...review}/>
                ))}

            </div>

        </div >
    );
}
export default CoursePage;