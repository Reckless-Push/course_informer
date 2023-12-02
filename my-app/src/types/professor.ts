export interface Professor {
  id: number | null
  firstName: string
  lastName: string
}

export interface ProfessorResponse {
  professor_table: Professor[]
}
