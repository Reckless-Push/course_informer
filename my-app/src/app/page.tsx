"use client"
import React from 'react';
import useFetchData from '@/hooks/useFetchData';
import {ProfessorResponse} from '@/types/professor';
import {UserResponse} from "@/types/user";
import {CourseResponse} from "@/types/course";
import {ReviewResponse} from "@/types/review";

export default function Home() {
    const {
        data: professorData,
        loading: professorLoading,
        error: professorError
    } = useFetchData<ProfessorResponse>('https://localhost:8443/professor');
    const {
        data: userData,
        loading: userLoading,
        error: userError
    } = useFetchData<UserResponse>('https://localhost:8443/user');
    const {
        data: courseData,
        loading: courseLoading,
        error: courseError
    } = useFetchData<CourseResponse>('https://localhost:8443/course');
    const {
        data: reviewData,
        loading: reviewLoading,
        error: reviewError
    } = useFetchData<ReviewResponse>('https://localhost:8443/review');

    if (professorLoading || userLoading || courseLoading || reviewLoading) return <div>Loading...</div>;
    if (professorError || userError || courseError || reviewError) return <div>Error: {professorError?.message || userError?.message || courseError?.message || reviewError?.message}</div>;

    return (
        <main>
            <h2>Professors:</h2>
            {professorData?.professor_table.map(professor => (
                <div key={professor.id}>
                    {professor.firstName} {professor.lastName}
                </div>
            ))}

            <br/>

            <h2>Users:</h2>
            {userData?.user_table.map(user => (
                <div key={user.id}>
                    {user.firstName} {user.lastName} {user.email}
                </div>
            ))}

            <br/>

            <h2>Courses:</h2>
            {courseData?.course_table.map(course => (
                <div key={course.cicsId}>
                    {course.name} {course.description} {course.credits} {JSON.stringify(course.semestersOffered)}
                </div>
            ))}

            <br/>

            <h2>Reviews:</h2>
            {reviewData?.review_table.map(review => (
                <div key={review.id}>
                    {JSON.stringify(review.professor)} {JSON.stringify(review.course)} {review.userId} {review.difficulty} {review.quality}
                </div>
            ))}
        </main>
    );
}