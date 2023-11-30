import React from 'react'
import useFetchData from '@/hooks/useFetchData'
import { CourseResponse } from '@/types/course'
import CourseTable from '@/components/CourseTable'

const CoursesComponent = () => {
  const { data, loading, error } = useFetchData<CourseResponse>(
    'https://localhost:8443/course'
  )

  if (loading) return <div>Loading courses...</div>
  if (error) return <div>Error: {error.message}</div>

  return (
    <>
      <br />
      {data && <CourseTable data={data} />}
    </>
  )
}

export default CoursesComponent
