"use client";
import Review from "@/app/components/Review";
import styles from "@/app/user/user.module.css";
import useFetchData from "@/app/hooks/useFetchData";
import { ReviewResponse } from "@/types/review";
import { User } from "@/types/user";
import { ComponentStates } from "@/types/ComponentStates";
import CourseCard from "@/app/components/CourseCard";
import { useEffect, useState } from "react";
import axios from "axios";

interface ProfilePageProps {
  onUserInputChange: any;
  onToggleComponent: (component: keyof ComponentStates) => void;
  onHome: () => void;
  componentStates: {
    courses: boolean;
    reviews: boolean;
    courseDashboard: boolean;
    login: boolean;
    user: boolean;
  };
}

function ProfilePage({
  onUserInputChange,
  onToggleComponent,
  onHome,
  componentStates,
}: ProfilePageProps) {
  const {
    data: userData,
    loading: userLoading,
    error: userError,
  } = useFetchData<User>(process.env.NEXT_PUBLIC_BASE_URL + "/user/current");
  const {
    data: reviewData,
    loading: reviewLoading,
    error: reviewError,
  } = useFetchData<ReviewResponse>(
    process.env.NEXT_PUBLIC_BASE_URL + "/review/user"
  );

  const [favoriteCourses, setFavoriteCourses] = useState<number[]>([]);

  const fetchFavoriteCourses = () => {
    axios
      .get(process.env.NEXT_PUBLIC_BASE_URL + "/user/current/favorites")
      .then((response) => setFavoriteCourses(response.data))
      .catch((error) => console.error(error));
  };

  useEffect(() => {
    fetchFavoriteCourses();
  }, []);

  if (userLoading) return <div>Loading User...</div>;
  if (userError) return <div>Error:{userError?.message}</div>;
  if (reviewLoading) return <div>Loading Reviews...</div>;
  if (reviewError) return <div>Error:{reviewError?.message}</div>;

  return (
    <div className={styles.UserPage}>
      <div className={styles.main}>
        {userData && (
          <>
            <div className={styles.UserName}>
              {userData.firstName} {userData.lastName}
            </div>
            <div className={styles.heading}>Your favorite courses:</div>
            <div>
              {userData?.favoriteCourses.map((course) => {
                course.semestersOffered.map((semesterInfo) => {
                  const { season, year } = semesterInfo;
                  return `${season} ${year}`;
                });
                return (
                  <CourseCard
                    key={course.cicsId}
                    course={course}
                    fetchFavoriteCourses={fetchFavoriteCourses}
                    favoriteCourses={favoriteCourses}
                    onUserInputChange={onUserInputChange}
                    onToggleComponent={onToggleComponent}
                    onHome={onHome}
                    componentStates={componentStates}
                  />
                );
              })}
            </div>
            <div className={styles.heading}>Your reviews:</div>
            {reviewData &&
              reviewData.review_table.map((review) => (
                <Review key={review.id} {...review} />
              ))}
          </>
        )}
      </div>
    </div>
  );
}

export default ProfilePage;
