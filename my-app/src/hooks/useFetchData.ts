import { useState, useEffect } from 'react';
import axios, { AxiosError } from 'axios';

// Define a generic type for useFetchData
const useFetchData = <T>(url: string) => {
    const [data, setData] = useState<T | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<AxiosError | null>(null);

    useEffect(() => {
        const fetchData = () => {
            axios.get<T>(url)
                .then(response => {
                    setData(response.data);
                    setLoading(false);
                })
                .catch(err => {
                    setError(err as AxiosError);
                    setLoading(false);
                });
        };

        fetchData();
    }, [url]);

    return { data, loading, error };
};

export default useFetchData;