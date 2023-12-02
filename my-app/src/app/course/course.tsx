"use client";
import CourseDesc from "@/app/components/CourseDesc";
import Rating from "@/app/components/Rating";
import Review from "@/app/components/Review";
import styles from "@/app/course/course.module.css";
import { ComponentStates } from '@/types/ComponentStates'

interface CoursePageProps {
    onToggleComponent: (component: keyof ComponentStates) => void
    onHome: () => void
    componentStates: {
        courses: boolean
        reviews: boolean
        courseDashboard: boolean,
        login: boolean
    }
}

function CoursePage({onToggleComponent,onHome,componentStates,}: CoursePageProps) {

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
                <Rating onToggleComponent={onToggleComponent}
                    onHome={onHome}
                    componentStates={componentStates} />

                <Review></Review>
                <Review></Review>
                <Review></Review>
                <Review></Review>

            </div>

        </div >
    );
}
export default CoursePage;