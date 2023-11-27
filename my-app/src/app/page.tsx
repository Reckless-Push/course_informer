"use client"
import React from 'react';
import useFetchData from '@/hooks/useFetchData'; // Adjust the path based on your folder structure
import { ApiResponse } from '@/types/apiResponse';

export default function Home() {
    const { data, loading, error } = useFetchData<ApiResponse>('https://localhost:8443/professor');

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error.message}</div>;

    if (data) {
        return (
            <main>
                {/* Render your data here */}
                {data.professor_table.map(professor => (
                    <div key={professor.id}>
                        {professor.firstName} {professor.lastName}
                    </div>
                ))}
            </main>
        );
    } else {
        return <div>No data available</div>;
    }
}