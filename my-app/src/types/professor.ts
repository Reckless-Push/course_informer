export interface Professor {
    id: number;
    firstName: string;
    lastName: string;
}

export interface ProfessorResponse {
    professor_table: Professor[];
}