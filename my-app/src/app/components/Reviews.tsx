import React, { useEffect, useState } from "react";
import useFetchData from "../hooks/useFetchData";
import { ReviewResponse } from "@/types/review";
import Review from "@/app/components/Review";
import Rating from "@/app/components/Rating";

interface ReviewsProps {
  courseId: number;
  instructorId?: number;
}

const Reviews: React.FC<ReviewsProps> = ({ courseId, instructorId }) => {
  const [reviews, setReviews] = useState<Review[]>([]);
  const [qualityScore, setQualityScore] = useState<number[]>([]);
  const [difficultyScore, setDifficultyScore] = useState<number[]>([]);

  const {
    data: reviewData,
    loading: reviewLoading,
    error: reviewError,
  } = useFetchData<ReviewResponse>(
    instructorId
      ? process.env.NEXT_PUBLIC_BASE_URL +
          `/review/professor/${instructorId}/${courseId}`
      : process.env.NEXT_PUBLIC_BASE_URL + `/review/course/${courseId}`
  );

  useEffect(() => {
    // Clear the state when courseId or instructorId changes
    setReviews([]);
    setQualityScore([]);
    setDifficultyScore([]);

    if (reviewData) {
      const newReviews: Review[] = [];
      const newQualityScore: number[] = [];
      const newDifficultyScore: number[] = [];

      reviewData.review_table.forEach((review) => {
        newReviews.push(review);
        newQualityScore.push(review.quality);
        newDifficultyScore.push(review.difficulty);
      });

      setReviews(newReviews);
      setQualityScore(newQualityScore);
      setDifficultyScore(newDifficultyScore);
    }
  }, [reviewData, courseId, instructorId]); // Add courseId and instructorId to the dependency array

  if (reviewLoading) return <div>Loading...</div>;
  if (reviewError) return <div>Error:{reviewError?.message}</div>;

  return (
    <div
      key={instructorId ? `${instructorId}-${courseId}` : `course-${courseId}`}
    >
      {reviews.length > 0 && (
        <Rating
          key={
            instructorId ? `${instructorId}-${courseId}` : `course-${courseId}`
          }
          id={
            instructorId ? `${instructorId}-${courseId}` : `course-${courseId}`
          }
          quality={qualityScore}
          difficulty={difficultyScore}
        />
      )}
      {reviews.length > 0 &&
        reviews.map((review) => <Review key={review.id} {...review} />)}
    </div>
  );
};

export default Reviews;
