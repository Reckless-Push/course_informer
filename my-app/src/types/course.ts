import { Professor } from '@/types/professor'
import { Semester } from '@/types/semester'

export interface Course {
  cicsId: number | null
  name: string
  description: string
  credits: number
  courseLevel: number
  undergraduateRequirements: Course[]
  semestersOffered: Semester[]
  professors: Professor[]
}

export interface CourseResponse {
  course_table: Course[]
}
