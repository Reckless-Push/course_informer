import { Professor } from "@/types/professor";
import { Semester } from "@/types/semester";

export interface Course {
  cicsId: number;
  name: string;
  description: string;
  credits: number;
  courseLevel: number;
  undergraduateRequirement: Course[];
  graduateRequirement: Course[];
  semestersOffered: Semester[];
  professors: Professor[];
}

export interface CourseResponse {
  course_table: Course[];
}
