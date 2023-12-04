import React, { useEffect, useState } from 'react'
import {
  Box,
  Button,
  Checkbox,
  FormControlLabel,
  MenuItem,
  Select,
  SelectChangeEvent,
  TextField,
  Typography,
} from '@mui/material'
import usePostData from '@/hooks/usePostData'
import useFetchData from '@/hooks/useFetchData'
import { Course, CourseResponse } from '@/types/course'
import { Professor, ProfessorResponse } from '@/types/professor'
import { Review } from '@/types/review'

const ReviewForm = () => {
  const initialReviewState: Review = {
    id: 0,
    professor: null,
    course: null,
    userId: null,
    date: null,
    difficulty: 0,
    quality: 0,
    comment: '',
    fromRmp: false,
    forCredit: false,
    attendance: false,
    textbook: false,
    grade: 'C',
  }

  const [review, setReview] = useState<Review>(initialReviewState)
  const [selectedCourse, setSelectedCourse] = useState<string>('')
  const [selectedProfessor, setSelectedProfessor] = useState<string>('')
  const [isSubmitClicked, setIsSubmitClicked] = useState(false)
  const { data, loading, error } = usePostData<Review, Review>(
    'https://localhost:8443/review',
    review,
    isSubmitClicked
  )
  const { data: coursesData } = useFetchData<CourseResponse>(
    'https://localhost:8443/course'
  )
  const { data: professorsData } = useFetchData<ProfessorResponse>(
    'https://localhost:8443/professor'
  )

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setReview({ ...review, [e.target.name]: e.target.value })
  }

  const handleCourseChange = (event: SelectChangeEvent) => {
    setSelectedCourse(event.target.value as string)
  }

  const handleProfessorChange = (event: SelectChangeEvent) => {
    setSelectedProfessor(event.target.value as string)
  }

  useEffect(() => {
    if (data) {
      setIsSubmitClicked(false)
    }
  }, [data])

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    const selectedProfessorObject = professorsData?.professor_table.find(
      (prof) => prof.firstName === selectedProfessor
    )

    const selectedCourseObject = coursesData?.course_table.find(
      (course) => course.name === selectedCourse
    )

    setReview({
      ...review,
      professor: selectedProfessorObject ? selectedProfessorObject : null,
      course: selectedCourseObject ? selectedCourseObject : null,
    })
    console.log(review) // Log the review object
    setIsSubmitClicked(true)
  }

  return (
    <Box
      component="form"
      onSubmit={handleSubmit}
      className="bg-gradient-to-r from-[rgb(var(--background-start-rgb))] to-[rgb(var(--background-end-rgb))] text-white p-4 rounded-lg shadow-md"
    >
      <Select
        className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
        value={selectedProfessor}
        onChange={handleProfessorChange}
      >
        {professorsData?.professor_table.map((professor: Professor) => (
          <MenuItem key={professor.id} value={professor.firstName}>
            {professor.firstName} {professor.lastName}
          </MenuItem>
        ))}
      </Select>
      <Select
        className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
        value={selectedCourse}
        onChange={handleCourseChange}
      >
        {coursesData?.course_table.map((course: Course) => (
          <MenuItem key={course.cicsId} value={course.name}>
            {course.name}
          </MenuItem>
        ))}
      </Select>
      <TextField
        className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
        name="difficulty"
        label="Difficulty"
        type="number"
        value={review.difficulty}
        onChange={handleChange}
        fullWidth
        margin="normal"
        id="difficulty"
        autoComplete="5"
        required
      />
      <TextField
        className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
        name="quality"
        label="Quality"
        type="number"
        value={review.quality}
        onChange={handleChange}
        fullWidth
        margin="normal"
        id="quality"
        autoComplete="3"
        required
      />
      <TextField
        className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
        name="comment"
        label="Comment"
        type="text"
        value={review.comment}
        onChange={handleChange}
        fullWidth
        margin="normal"
        id="comment"
        autoComplete="This course is great!"
        required
      />
      <FormControlLabel
        control={
          <Checkbox
            checked={review.fromRmp}
            onChange={(e) =>
              setReview({ ...review, fromRmp: e.target.checked })
            }
            name="fromRmp"
          />
        }
        label="From Rate My Professor"
      />

      <FormControlLabel
        control={
          <Checkbox
            checked={review.forCredit}
            onChange={(e) =>
              setReview({ ...review, forCredit: e.target.checked })
            }
            name="forCredit"
          />
        }
        label="For Credit"
      />

      <FormControlLabel
        control={
          <Checkbox
            checked={review.attendance}
            onChange={(e) =>
              setReview({ ...review, attendance: e.target.checked })
            }
            name="attendance"
          />
        }
        label="Attendance Required"
      />

      <FormControlLabel
        control={
          <Checkbox
            checked={review.textbook}
            onChange={(e) =>
              setReview({ ...review, textbook: e.target.checked })
            }
            name="textbook"
          />
        }
        label="Textbook Required"
      />

      <Button type="submit" fullWidth variant="contained" sx={{ mt: 3, mb: 2 }}>
        Submit
      </Button>
      {loading && <Typography>Loading...</Typography>}
      {error && <Typography color="error">Error: {error.message}</Typography>}
      {data && <Typography>Success!</Typography>}
    </Box>
  )
}

export default ReviewForm
