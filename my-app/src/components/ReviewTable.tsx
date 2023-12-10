import React from 'react'
import Table from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell from '@mui/material/TableCell'
import TableContainer from '@mui/material/TableContainer'
import TableHead from '@mui/material/TableHead'
import TableRow from '@mui/material/TableRow'
import Paper from '@mui/material/Paper'
import Typography from '@mui/material/Typography'
import { ReviewsTableProps } from '@/types/ReviewsTableProps'

const ReviewsTable: React.FC<ReviewsTableProps> = ({ data }) => {
  return (
    <TableContainer component={Paper}>
      <Typography variant="h6" component="div" style={{ padding: '16px' }}>
        {'Reviews'}
      </Typography>
      <Table aria-label="reviews table">
        <TableHead>
          <TableRow>
            <TableCell>Professor</TableCell>
            <TableCell>Course</TableCell>
            <TableCell>User ID</TableCell>
            <TableCell>Date</TableCell>
            <TableCell>Difficulty</TableCell>
            <TableCell>Quality</TableCell>
            <TableCell>Comment</TableCell>
            <TableCell>From RMP</TableCell>
            <TableCell>For Credit</TableCell>
            <TableCell>Attendance</TableCell>
            <TableCell>Textbook</TableCell>
            <TableCell>Grade</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.review_table.map((review) => (
            <TableRow key={review.id}>
              <TableCell>{JSON.stringify(review.professor)}</TableCell>
              <TableCell>{JSON.stringify(review.course)}</TableCell>
              <TableCell>{review.userId}</TableCell>
              <TableCell>{review.date}</TableCell>
              <TableCell>{review.difficulty}</TableCell>
              <TableCell>{review.quality}</TableCell>
              <TableCell>{review.comment}</TableCell>
              <TableCell>{review.fromRmp}</TableCell>
              <TableCell>{review.forCredit}</TableCell>
              <TableCell>{review.attendance}</TableCell>
              <TableCell>{review.textbook}</TableCell>
              <TableCell>{review.grade}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  )
}

export default ReviewsTable