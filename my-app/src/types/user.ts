import { Course } from '@/types/course'
import { Review } from '@/types/review'

export interface User {
  id: string
  firstName: string
  lastName: string
  email: string
  favoriteCourses: Course[]
  reviews: Review[]
}

export interface UserResponse {
  user_table: User[]
}
