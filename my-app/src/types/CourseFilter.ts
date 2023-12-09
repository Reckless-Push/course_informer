import { Semester } from '@/types/semester'

export interface CourseFilter {
  minCredits: number
  maxCredits: number
  courseLevel: number
  semestersOffered: Semester[]
}
