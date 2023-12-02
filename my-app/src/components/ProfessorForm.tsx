import React, { useEffect, useState } from 'react'
import { Box, Button, TextField, Typography } from '@mui/material'
import usePostData from '@/hooks/usePostData'
import { Professor } from '@/types/professor'

const ProfessorForm = () => {
  const [professor, setProfessor] = useState<Professor>({
    id: null,
    firstName: '',
    lastName: '',
  })
  const [isSubmitClicked, setIsSubmitClicked] = useState(false)
  const { data, loading, error } = usePostData<Professor, Professor>(
    'https://localhost:8443/professor',
    professor,
    isSubmitClicked // Assuming usePostData accepts a trigger
  )

  useEffect(() => {
    if (data) {
      // Handle successful submission
      // Reset the trigger
      setIsSubmitClicked(false)
    }
  }, [data])

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setProfessor({ ...professor, [e.target.name]: e.target.value })
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    setIsSubmitClicked(true) // Trigger the submission
  }

  return (
    <Box
      component="form"
      onSubmit={handleSubmit}
      className="bg-gradient-to-r from-[rgb(var(--background-start-rgb))] to-[rgb(var(--background-end-rgb))] text-white p-4 rounded-lg shadow-md"
    >
      <TextField
        className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
        margin="normal"
        required
        fullWidth
        name="firstName"
        label="First Name"
        type="text"
        id="firstName"
        autoComplete="firstName"
        value={professor.firstName}
        onChange={handleChange}
      />
      <TextField
        className="mb-4 w-full bg-[#FFFFFF] text-[#000000] border border-[rgb(var(--neutral-dark-gray-rgb))] rounded"
        margin="normal"
        required
        fullWidth
        name="lastName"
        label="Last Name"
        type="text"
        id="lastName"
        autoComplete="lastName"
        value={professor.lastName}
        onChange={handleChange}
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

export default ProfessorForm
