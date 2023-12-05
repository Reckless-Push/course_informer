import { useEffect, useState } from 'react'
import axios, { AxiosError } from 'axios'

const usePostData = <T, U>(
  url: string,
  postData: U,
  isSubmitClicked: boolean
) => {
  const [data, setData] = useState<T | null>(null)
  const [loading, setLoading] = useState<boolean>(false) // Initial loading is false
  const [error, setError] = useState<AxiosError | null>(null)

  useEffect(() => {
    if (isSubmitClicked) {
      setLoading(true) // Set loading to true when starting the submission
      axios
        .post<T>(url, postData)
        .then((response) => {
          setData(response.data)
        })
        .catch((err) => {
          setError(err as AxiosError)
        })
        .finally(() => {
          setLoading(false) // Reset loading state after the request completes
        })
    }
  }, [url, postData, isSubmitClicked]) // Dependency array

  return { data, loading, error }
}

export default usePostData