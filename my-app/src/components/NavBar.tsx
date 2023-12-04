import React, { useEffect, useState } from 'react'
import AppBar from '@mui/material/AppBar'
import Toolbar from '@mui/material/Toolbar'
import Button from '@mui/material/Button'
import Typography from '@mui/material/Typography'
import Menu from '@mui/material/Menu'
import MenuItem from '@mui/material/MenuItem'
import HomeIcon from '@mui/icons-material/Home'
import { ComponentStates } from '@/types/ComponentStates'
import { NavBarProps } from '@/types/NavBarProps'

const NavBar = ({
  onToggleComponent,
  onHome,
  componentStates,
}: NavBarProps) => {
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null)
  const [formAnchorEl, setFormAnchorEl] = useState<null | HTMLElement>(null)
  const tablesOpen = Boolean(anchorEl)
  const formsOpen = Boolean(formAnchorEl)
  const [isLoggedIn, setIsLoggedIn] = useState(false)

  const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget)
  }

  const handleFormMenu = (event: React.MouseEvent<HTMLElement>) => {
    setFormAnchorEl(event.currentTarget)
  }

  const handleClose = () => {
    setAnchorEl(null)
    setFormAnchorEl(null)
  }

  const handleMenuItemClick = (component: keyof ComponentStates) => {
    onToggleComponent(component)
    handleClose()
  }

  useEffect(() => {
    // Function to check login status
    const checkLoginStatus = () => {
      fetch('https://localhost:8443/hello')
        .then((response) => {
          setIsLoggedIn(response.ok)
        })
        .catch((error) => {
          console.error('Error checking login status', error)
          setIsLoggedIn(false)
        })
    }

    checkLoginStatus()
  }, [])

  const handleLoginClick = () => {
    window.location.href = `https://localhost:8443/${
      isLoggedIn ? 'logout' : 'login'
    }`
  }

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Course Informer
        </Typography>

        <Button color="inherit" onClick={handleMenu}>
          Tables
        </Button>
        <Menu
          id="tables-menu"
          anchorEl={anchorEl}
          open={tablesOpen}
          onClose={handleClose}
        >
          <MenuItem onClick={() => handleMenuItemClick('professors')}>
            {componentStates.professors ? 'Hide' : 'Show'} Professors
          </MenuItem>
          <MenuItem onClick={() => handleMenuItemClick('users')}>
            {componentStates.users ? 'Hide' : 'Show'} Users
          </MenuItem>
          <MenuItem onClick={() => handleMenuItemClick('courses')}>
            {componentStates.courses ? 'Hide' : 'Show'} Courses
          </MenuItem>
          <MenuItem onClick={() => handleMenuItemClick('reviews')}>
            {componentStates.reviews ? 'Hide' : 'Show'} Reviews
          </MenuItem>
        </Menu>

        <Button color="inherit" onClick={handleFormMenu}>
          Forms
        </Button>
        <Menu
          id="forms-menu"
          anchorEl={formAnchorEl}
          open={formsOpen}
          onClose={handleClose}
        >
          <MenuItem onClick={() => handleMenuItemClick('profForm')}>
            {componentStates.profForm ? 'Hide' : 'Show'} Professor Form
          </MenuItem>
          <MenuItem onClick={() => handleMenuItemClick('courseForm')}>
            {componentStates.courseForm ? 'Hide' : 'Show'} Course Form
          </MenuItem>
          <MenuItem onClick={() => handleMenuItemClick('reviewForm')}>
            {componentStates.reviewForm ? 'Hide' : 'Show'} Review Form
          </MenuItem>
        </Menu>

        <Button color="inherit" onClick={onHome}>
          <HomeIcon />
        </Button>

        <Button color="inherit" onClick={handleLoginClick}>
          {isLoggedIn ? 'Logout' : 'Login'}
        </Button>
      </Toolbar>
    </AppBar>
  )
}

export default NavBar
