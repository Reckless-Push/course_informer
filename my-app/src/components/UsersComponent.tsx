import React from 'react'
import useFetchData from '@/hooks/useFetchData'
import { UserResponse } from '@/types/user'
import UsersTable from '@/components/UserTable'

const UsersComponent = () => {
  const { data, loading, error } = useFetchData<UserResponse>(
    'https://localhost:8443/user'
  )

  if (loading) return <div>Loading users...</div>
  if (error) return <div>Error: {error.message}</div>

  return (
    <>
      <br />
      {data && <UsersTable data={data} />}
    </>
  )
}

export default UsersComponent
