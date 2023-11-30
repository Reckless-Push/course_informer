import React from 'react'
import Table from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell from '@mui/material/TableCell'
import TableContainer from '@mui/material/TableContainer'
import TableHead from '@mui/material/TableHead'
import TableRow from '@mui/material/TableRow'
import Paper from '@mui/material/Paper'
import { ProfessorResponse } from '@/types/professor'
import Typography from '@mui/material/Typography'

interface ProfessorTableProps {
  data: ProfessorResponse
}

const ProfessorTable: React.FC<ProfessorTableProps> = ({ data }) => {
  return (
    <TableContainer component={Paper}>
      <Typography variant="h6" component="div" style={{ padding: '16px' }}>
        {'Professors'}
      </Typography>
      <Table aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>First Name</TableCell>
            <TableCell>Last Name</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.professor_table.map((professor) => (
            <TableRow key={professor.id}>
              <TableCell>{professor.firstName}</TableCell>
              <TableCell>{professor.lastName}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  )
}

export default ProfessorTable
