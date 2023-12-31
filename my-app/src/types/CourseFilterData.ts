import { Semester } from "@/types/semester";

export interface CourseFilterData {
  credits: number[];
  undergrad: boolean;
  grad: boolean;
  semestersOffered: Semester[];
}
