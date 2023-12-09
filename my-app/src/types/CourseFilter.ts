import { Semester } from '@/types/semester'

export interface Course {
  minCredits: number
  maxCredits: number
  courseLevel: number
  semestersOffered: Semester[]
}
