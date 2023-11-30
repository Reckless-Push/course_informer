import React from 'react'
import useFetchData from '@/hooks/useFetchData'
import { ReviewResponse } from '@/types/review'
import ReviewsTable from '@/components/ReviewTable'

const ReviewsComponent = () => {
  const { data, loading, error } = useFetchData<ReviewResponse>(
    'https://localhost:8443/review'
  )

  if (loading) return <div>Loading reviews...</div>
  if (error) return <div>Error: {error.message}</div>

  return (
    <>
      <br />
      {data && <ReviewsTable data={data} />}
    </>
  )
}

export default ReviewsComponent
