import {Professor} from "@/types/professor";
import {Course} from "@/types/course";

export interface Review {
    id: number;
    professor: Professor;
    course: Course;
    userId: number;
    date: string;
    difficulty: number;
    quality: number;
    tags: string[];
    comment: string;
    fromRmp: boolean;
}

export interface ReviewResponse {
    review_table: Review[];
}