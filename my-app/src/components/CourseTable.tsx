import React from 'react'
import Table from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell from '@mui/material/TableCell'
import TableContainer from '@mui/material/TableContainer'
import TableHead from '@mui/material/TableHead'
import TableRow from '@mui/material/TableRow'
import Paper from '@mui/material/Paper'
import Typography from '@mui/material/Typography'
import { CourseTableProps } from '@/types/CourseTableProps'

const CourseTable: React.FC<CourseTableProps> = ({ data }) => {
  return (
    <TableContainer component={Paper}>
      <Typography variant="h6" component="div" style={{ padding: '16px' }}>
        {'Courses'}
      </Typography>
      <Table aria-label="course table">
        <TableHead>
          <TableRow>
            <TableCell>Course Name</TableCell>
            <TableCell>Description</TableCell>
            <TableCell>Credits</TableCell>
            <TableCell>Course Level</TableCell>
            <TableCell>Undergraduate Requirements</TableCell>
            <TableCell>Graduate Requirements</TableCell>
            <TableCell>Semesters Offered</TableCell>
            <TableCell>Professors</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.course_table.map((course) => (
            <TableRow key={course.cicsId}>
              <TableCell>{course.name}</TableCell>
              <TableCell>{course.description}</TableCell>
              <TableCell>{course.credits}</TableCell>
              <TableCell>{course.courseLevel}</TableCell>
              <TableCell>
                {JSON.stringify(course.undergraduateRequirements)}
              </TableCell>
              <TableCell>
                {JSON.stringify(course.graduateRequirements)}
              </TableCell>
              <TableCell>{JSON.stringify(course.semestersOffered)}</TableCell>
              <TableCell>{JSON.stringify(course.professors)}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  )
}

export default CourseTable
