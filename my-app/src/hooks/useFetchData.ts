"use client"
// hooks/useFetchData.ts
import { useState, useEffect } from 'react';
import axios, { AxiosError } from 'axios';

// Define a generic type for useFetchData
const useFetchData = <T>(url: string) => {
    const [data, setData] = useState<T | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    // Using AxiosError for detailed error type
    const [error, setError] = useState<AxiosError | null>(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get<T>(url);
                setData(response.data);
                setLoading(false);
            } catch (err) {
                // Type assertion for error handling
                setError(err as AxiosError);
                setLoading(false);
            }
        };

        fetchData();
    }, [url]);

    return { data, loading, error };
};

export default useFetchData;
