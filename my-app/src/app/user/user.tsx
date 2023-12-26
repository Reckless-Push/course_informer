"use client";
import Review from "@/app/components/Review";
import styles from "@/app/user/user.module.css";
import useFetchData from "@/app/hooks/useFetchData";
import { ReviewResponse } from "@/types/review";
import { User } from "@/types/user";

function ProfilePage() {
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

  if (userLoading) return <div>Loading User...</div>;
  if (userError) return <div>Error:{userError?.message}</div>;
  if (reviewLoading) return <div>Loading Reviews...</div>;
  if (reviewError) return <div>Error:{reviewError?.message}</div>;

  return (
    <div className={styles.UserPage}>
      <div className={styles.main}>
        {userData && (
          <div className={styles.UserName}>
            {userData.firstName} {userData.lastName}
          </div>
        )}
        <div className={styles.heading}> Your reviews:</div>
        {reviewData &&
          reviewData.review_table.map((review) => (
            <Review key={review.id} {...review} />
          ))}
      </div>
    </div>
  );
}

export default ProfilePage;
