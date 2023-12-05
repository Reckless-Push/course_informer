import { Professor } from '@/types/professor'
import { Course } from '@/types/course'

export interface Review {
  id: number
  professor: Professor | null
  course: Course | null
  userId: string | null
  date: string | null
  difficulty: number
  quality: number
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