"use client";
import CourseDesc from "@/app/components/CourseDesc";
import Rating from "@/app/components/Rating";
import Review from "@/app/components/Review";
import styles from "@/app/course/course.module.css";
import { ComponentStates } from "@/types/ComponentStates";
import useFetchData from "../hooks/useFetchData";
import { ReviewResponse } from "@/types/review";
import { Course } from "@/types/course";
interface CoursePageProps {
  course_data: Course;
  onUserInputChange: any;
}

function CoursePage({ course_data, onUserInputChange }: CoursePageProps) {
  console.log("Which course:", course_data)
  const {
    data: reviewData,
    loading: reviewLoading,
    error: reviewError,
  } = useFetchData<ReviewResponse>("https://localhost:8443/review");

  if (Object.keys(course_data).length === 0 && course_data.constructor === Object) {
    return <div>No course data available.</div>;
  }
  

  if (reviewLoading) return <div>Loading...</div>;
  if (reviewError) return <div>Error:{reviewError?.message}</div>;

  const qualityscore = new Array();
  const difficultyscore = new Array();
  reviewData?.review_table.forEach((review) => {
    if (review.course && review.course.cicsId == course_data.cicsId) {
      qualityscore.push(review.quality);
      difficultyscore.push(review.difficulty);
    }
  });

  const handleClick = () => {
    onUserInputChange(course_data);
  };

  return (
    <div className={styles.CoursePage}>
      {/* <Navbar></Navbar> */}
      <div className={styles.main}>
        {course_data && <CourseDesc {...course_data}></CourseDesc>}

        {qualityscore.length > 0 && difficultyscore.length > 0 && (
          <Rating quality={qualityscore} difficulty={difficultyscore} />
        )}

        <div className={styles.RateThisCourse}>
          <button
            className={styles.RateThisCourseBtn}
            onClick={() => handleClick()}
          >
            Add review
          </button>
        </div>
        {reviewData?.review_table.map((review) => (
          <Review key={review.comment} {...review} />
        ))}
      </div>
    </div>
  );
}
export default CoursePage;
