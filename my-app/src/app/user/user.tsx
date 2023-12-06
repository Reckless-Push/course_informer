"use client";
import CourseDesc from "@/app/components/CourseDesc";
import Rating from "@/app/components/Rating";
import Review from "@/app/components/Review";
import styles from "@/app/user/user.module.css";
import { ComponentStates } from "@/types/ComponentStates";
import { Course } from "@/types/course";
import { UserResponse } from "@/types/user";
import useFetchData from "@/app/hooks/useFetchData";
import { ReviewResponse } from "@/types/review";

interface ProfileProps {
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
  onToggleComponent,
  onHome,
  componentStates,
}: ProfileProps) {
  const {
    data: userData,
    loading: userLoading,
    error: userError,
  } = useFetchData<UserResponse>("https://localhost:8443/user");
  const {
    data: reviewData,
    loading: reviewLoading,
    error: reviewError,
  } = useFetchData<ReviewResponse>("https://localhost:8443/review");

  if (userLoading) return <div>Loading...</div>;
  if (userError) return <div>Error:{userError?.message}</div>;
  let user = userData?.user_table[0];
  console.log(userData?.user_table);
  return (
    <div className={styles.UserPage}>
      {/* <Navbar></Navbar> */}
      <div className={styles.main}>
        {user && (
          <div className={styles.UserName}>
            {user.firstName} {user.lastName}
          </div>
        )}
        <div className={styles.heading}> Your reviews:</div>
        {user &&
          user.reviews.map((review) => <Review key={review.id} {...review} />)}
      </div>
    </div>
  );
}
export default ProfilePage;
