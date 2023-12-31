export interface Semester {
  season: string;
  year: number;
}

export interface SemesterResponse {
  semester_table: Semester[];
}
