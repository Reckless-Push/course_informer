import { Professor } from '@/types/professor'
import { Course } from '@/types/course'

export interface Review {
  id: number
  professor: Professor
  course: Course
  userId: string
  date: string
  difficulty: number
  quality: number
  tags: string[]
  comment: string
  fromRmp: boolean
  forCredit: boolean
  attendance: boolean
  textbook: boolean
  grade: string
}

export interface ReviewResponse {
  review_table: Review[]
}
