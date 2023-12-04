import React, { useEffect, useState } from 'react'
import {
  Box,
  Button,
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

const CourseForm = () => {
  const initialCourseState: Course = {
    cicsId: null,
    name: '',
    description: '',
    credits: 0,
    courseLevel: 0,
    undergraduateRequirements: [],
    semestersOffered: [],
    professors: [],
  }

  const [selectedSeason, setSelectedSeason] = useState('')
  const [selectedYear, setSelectedYear] = useState('')
  const [selectedUndergradRequirements, setSelectedUndergradRequirements] =
    useState<string[]>([])
  const [selectedProfessors, setSelectedProfessors] = useState<string[]>([])
  const [course, setCourse] = useState<Course>(initialCourseState)
  const [isSubmitClicked, setIsSubmitClicked] = useState(false)
  const { data, loading, error } = usePostData<Course, Course>(
    'https://localhost:8443/course',
    course,
    isSubmitClicked
  )
  const { data: coursesData } = useFetchData<CourseResponse>(
    'https://localhost:8443/course'
  )
  const { data: professorsData } = useFetchData<ProfessorResponse>(
    'https://localhost:8443/professor'
  )
  const currentYear = new Date().getFullYear()
  const years = [currentYear - 1, currentYear, currentYear + 1]

  const handleSeasonChange = (event: SelectChangeEvent) => {
    setSelectedSeason(event.target.value)
  }

  const handleYearChange = (event: SelectChangeEvent) => {
    setSelectedYear(event.target.value)
  }
  const handleUndergradRequirementsChange = (
    event: SelectChangeEvent<string[]>
  ) => {
    setSelectedUndergradRequirements(event.target.value as string[])
  }

  const handleProfessorChange = (event: SelectChangeEvent<string[]>) => {
    setSelectedProfessors(event.target.value as string[])
  }

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
    const selectedProfessorObjects = selectedProfessors
      .map((selectedName) =>
        professorsData?.professor_table.find(
          (prof) => prof.firstName === selectedName
        )
      )
      .filter((professor): professor is Professor => professor !== undefined)

    const selectedUndergradCourses = selectedUndergradRequirements
      .map((selectedName) =>
        coursesData?.course_table.find((course) => course.name === selectedName)
      )
      .filter((course): course is Course => course !== undefined)

    const semester = {
      season: selectedSeason.toUpperCase(),
      year: parseInt(selectedYear),
    }

    setCourse({
      ...course,
      professors: selectedProfessorObjects,
      undergraduateRequirements: selectedUndergradCourses,
      semestersOffered: [...course.semestersOffered, semester],
    })

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
      <TextField
        className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
        name="credits"
        label="Credits"
        type="number"
        value={course.credits}
        onChange={handleChange}
        fullWidth
        margin="normal"
        id="credits"
        autoComplete="3"
        required
      />
      <TextField
        className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
        name="courseLevel"
        label="Course Level"
        type="number"
        value={course.courseLevel}
        onChange={handleChange}
        fullWidth
        margin="normal"
        id="courseLevel"
        autoComplete="1"
        required
      />
      <Select
        className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
        multiple
        value={selectedProfessors}
        onChange={handleProfessorChange}
        renderValue={(selected) => selected.join(', ')}
      >
        {professorsData?.professor_table.map((professor: Professor) => (
          <MenuItem key={professor.id} value={professor.firstName}>
            {professor.firstName} {professor.lastName}
          </MenuItem>
        ))}
      </Select>
      <Select
        className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
        multiple
        value={selectedUndergradRequirements}
        onChange={handleUndergradRequirementsChange}
        renderValue={(selected) => selected.join(', ')}
      >
        {coursesData?.course_table.map((course: Course) => (
          <MenuItem key={course.cicsId} value={course.name}>
            {course.name}
          </MenuItem>
        ))}
      </Select>
      <Select
        className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
        value={selectedSeason}
        onChange={handleSeasonChange}
      >
        {['Spring', 'Summer', 'Fall', 'Winter'].map((season) => (
          <MenuItem key={season} value={season}>
            {season}
          </MenuItem>
        ))}
      </Select>

      <Select
        className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
        value={selectedYear}
        onChange={handleYearChange}
      >
        {years.map((year) => (
          <MenuItem key={year} value={year}>
            {year}
          </MenuItem>
        ))}
      </Select>
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
