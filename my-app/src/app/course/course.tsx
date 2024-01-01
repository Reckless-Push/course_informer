import CourseDesc from "@/app/components/CourseDesc";
import Rating from "@/app/components/Rating";
import Review from "@/app/components/Review";
import styles from "@/app/course/course.module.css";
import useFetchData from "../hooks/useFetchData";
import { ReviewResponse } from "@/types/review";
import { Course } from "@/types/course";
import { ComponentStates } from "@/types/ComponentStates";

interface CoursePageProps {
  course_data: Course;
  onUserInputChange: any;
}

function CoursePage({
  course_data,
  onUserInputChange,
  onToggleComponent,
}: CoursePageProps & {
  onToggleComponent: (component: keyof ComponentStates) => void;
}) {
  console.log("Which course:", course_data);
  const {
    data: reviewData,
    loading: reviewLoading,
    error: reviewError,
  } = useFetchData<ReviewResponse>(
    process.env.NEXT_PUBLIC_BASE_URL + "/review"
  );

  if (
    Object.keys(course_data).length === 0 &&
    course_data.constructor === Object
  ) {
    return <div>No course data available.</div>;
  }

  if (reviewLoading) return <div>Loading...</div>;
  if (reviewError) return <div>Error:{reviewError?.message}</div>;

  const qualityScore: number[] = [];
  const difficultyScore: number[] = [];
  const reviews: Review[] = [];
  reviewData?.review_table.forEach((review) => {
    if (review.course && review.course.cicsId == course_data.cicsId) {
      reviews.push(review);
      qualityScore.push(review.quality);
      difficultyScore.push(review.difficulty);
    }
  });

  const handleClick = () => {
    onUserInputChange(course_data);
  };

  const handleBackClick = () => {
    onToggleComponent("courses");
    onToggleComponent("courseDashboard");
  };

  return (
    <div className={styles.CoursePage}>
      <button className={styles.backButton} onClick={handleBackClick}>
        Back
      </button>
      <div className={styles.main}>
        {course_data && <CourseDesc {...course_data}></CourseDesc>}

        {reviews.length > 0 && (
          <Rating quality={qualityScore} difficulty={difficultyScore} />
        )}

        <div className={styles.RateThisCourse}>
          <button
            className={styles.RateThisCourseBtn}
            onClick={() => handleClick()}
          >
            Add review
          </button>
        </div>
        {reviews.length > 0 &&
          reviews.map((review) => <Review key={review.comment} {...review} />)}
      </div>
    </div>
  );
}

export default CoursePage;
