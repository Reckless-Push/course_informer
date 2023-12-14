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
  } = useFetchData<User>("https://localhost:8443/user/current");
  const {
    data: reviewData,
    loading: reviewLoading,
    error: reviewError,
  } = useFetchData<ReviewResponse>("https://localhost:8443/review/user");

  if (userLoading) return <div>Loading...</div>;
  if (userError) return <div>Error:{userError?.message}</div>;
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
