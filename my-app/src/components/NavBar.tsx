import React, { useState } from 'react'
import AppBar from '@mui/material/AppBar'
import Toolbar from '@mui/material/Toolbar'
import Button from '@mui/material/Button'
import Typography from '@mui/material/Typography'
import { ComponentStates } from '@/types/ComponentStates'
import Menu from '@mui/material/Menu'
import MenuItem from '@mui/material/MenuItem'
import HomeIcon from '@mui/icons-material/Home'

interface NavBarProps {
  onToggleComponent: (component: keyof ComponentStates) => void
  onHome: () => void
  componentStates: ComponentStates
}

const NavBar = ({
  onToggleComponent,
  onHome,
  componentStates,
}: NavBarProps) => {
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null)
  const open = Boolean(anchorEl)

  const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget)
  }

  const handleClose = () => {
    setAnchorEl(null)
  }

  const handleMenuItemClick = (component: keyof ComponentStates) => {
    onToggleComponent(component)
    handleClose()
  }

  const handleLoginClick = () => {
    window.location.href = 'https://localhost:8443/login'
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
          open={open}
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
        <Button color="inherit" onClick={onHome}>
          <HomeIcon />
        </Button>
        <Button color="inherit" onClick={handleLoginClick}>
          login
        </Button>
      </Toolbar>
    </AppBar>
  )
}

export default NavBar
