import React, { useEffect, useState } from 'react'
import { Box, Button, TextField, Typography } from '@mui/material'
import usePostData from '@/hooks/usePostData'
import { Course } from '@/types/course'

const CourseForm = () => {
  const initialCourseState: Course = {
    cicsId: null,
    name: '',
    description: '',
    credits: 0,
    courseLevel: 0,
    undergraduateRequirements: [],
    graduateRequirements: [],
    semestersOffered: [],
    professors: [],
  }
  const [course, setCourse] = useState<Course>(initialCourseState)
  const [isSubmitClicked, setIsSubmitClicked] = useState(false)
  const { data, loading, error } = usePostData<Course, Course>(
    'https://localhost:8443/course',
    course,
    isSubmitClicked
  )

  useEffect(() => {
    if (data) {
      setIsSubmitClicked(false)
    }
  }, [data])

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCourse({ ...course, [e.target.name]: e.target.value })
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    setIsSubmitClicked(true)
  }

  return (
    <Box
      component="form"
      onSubmit={handleSubmit}
      className="bg-gradient-to-r from-[rgb(var(--background-start-rgb))] to-[rgb(var(--background-end-rgb))] text-white p-4 rounded-lg shadow-md"
    >
      <TextField
        className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
        name="cicsId"
        label="CICS ID"
        type="number"
        value={course.cicsId}
        onChange={handleChange}
        fullWidth
        margin="normal"
        id="cicsid"
        autoComplete="520"
        required
      />
      <TextField
        className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
        name="name"
        label="Course Name"
        type="text"
        value={course.name}
        onChange={handleChange}
        fullWidth
        margin="normal"
        id="coursename"
        autoComplete="Theory and Practice of Software Engineering"
        required
      />
      <TextField
        className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
        name="description"
        label="Description"
        type="text"
        value={course.description}
        onChange={handleChange}
        fullWidth
        margin="normal"
        id="description"
        autoComplete="Our Course"
        required
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

export default CourseForm
