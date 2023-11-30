import React from 'react'
import useFetchData from '@/hooks/useFetchData'
import { ProfessorResponse } from '@/types/professor'
import ProfessorTable from '@/components/ProfessorTable'

const ProfessorsComponent = () => {
  const { data, loading, error } = useFetchData<ProfessorResponse>(
    'https://localhost:8443/professor'
  )

  if (loading) return <div>Loading professors...</div>
  if (error) return <div>Error: {error.message}</div>

  return (
    <>
      <br />
      {data && <ProfessorTable data={data} />}
    </>
  )
}

export default ProfessorsComponent
