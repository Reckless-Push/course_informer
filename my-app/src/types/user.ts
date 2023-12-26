import { Course } from "@/types/course";
import { Review } from "@/types/review";

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  favoriteCourses: Course[];
  reviews: Review[];
}
