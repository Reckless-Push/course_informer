import React from 'react'
import Table from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell from '@mui/material/TableCell'
import TableContainer from '@mui/material/TableContainer'
import TableHead from '@mui/material/TableHead'
import TableRow from '@mui/material/TableRow'
import Paper from '@mui/material/Paper'
import { UserResponse } from '@/types/user'

interface UsersTableProps {
  data: UserResponse
}

const UsersTable: React.FC<UsersTableProps> = ({ data }) => {
  return (
    <TableContainer component={Paper}>
      <Table aria-label="users table">
        <TableHead>
          <TableRow>
            <TableCell>First Name</TableCell>
            <TableCell>Last Name</TableCell>
            <TableCell>Email</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.user_table.map((user) => (
            <TableRow key={user.id}>
              <TableCell>{user.firstName}</TableCell>
              <TableCell>{user.lastName}</TableCell>
              <TableCell>{user.email}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  )
}

export default UsersTable
