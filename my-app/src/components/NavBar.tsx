import React from 'react'
import AppBar from '@mui/material/AppBar'
import Toolbar from '@mui/material/Toolbar'
import Button from '@mui/material/Button'
import Typography from '@mui/material/Typography'
import { ComponentStates } from '@/types/ComponentStates'

interface NavBarProps {
  onToggleComponent: (component: keyof ComponentStates) => void
  onHome: () => void
  componentStates: {
    professors: boolean
    users: boolean
    courses: boolean
    reviews: boolean
  }
}

const NavBar = ({
  onToggleComponent,
  onHome,
  componentStates,
}: NavBarProps) => {
  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          My App
        </Typography>

        {/* Buttons for each component */}
        <Button color="inherit" onClick={() => onToggleComponent('professors')}>
          {componentStates.professors ? 'Hide' : 'Show'} Professors
        </Button>
        <Button color="inherit" onClick={() => onToggleComponent('users')}>
          {componentStates.users ? 'Hide' : 'Show'} Users
        </Button>
        <Button color="inherit" onClick={() => onToggleComponent('courses')}>
          {componentStates.courses ? 'Hide' : 'Show'} Courses
        </Button>
        <Button color="inherit" onClick={() => onToggleComponent('reviews')}>
          {componentStates.reviews ? 'Hide' : 'Show'} Reviews
        </Button>

        <Button color="inherit" onClick={onHome}>
          Home
        </Button>
      </Toolbar>
    </AppBar>
  )
}

export default NavBar
