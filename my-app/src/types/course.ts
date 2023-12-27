import { Professor } from "@/types/professor";
import { Semester } from "@/types/semester";

export interface Course {
  id: number;
  cicsId: string;
  courseLevel: number;
  department: string;
  name: string;
  description: string;
  credits: number;
  instructors: Professor[];
  prerequisites: string;
  semestersOffered: Semester[];
}

export interface CourseResponse {
  course_table: Course[];
}
