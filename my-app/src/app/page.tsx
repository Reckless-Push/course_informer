'use client'
import React, { useState } from 'react'
import NavBar from '../components/NavBar'
import ProfessorsComponent from '../components/ProfessorsComponent'
import UsersComponent from '../components/UsersComponent'
import CoursesComponent from '../components/CoursesComponent'
import ReviewsComponent from '../components/ReviewsComponent'
import { ComponentStates } from '@/types/ComponentStates'
type ComponentKey = 'professors' | 'users' | 'courses' | 'reviews'

const Home = () => {
  const [componentStates, setComponentStates] = useState<ComponentStates>({
    professors: false,
    users: false,
    courses: false,
    reviews: false,
  })

  const onToggleComponent = (component: ComponentKey) => {
    setComponentStates((prev) => ({ ...prev, [component]: !prev[component] }))
  }

  const onHome = () => {
    setComponentStates({
      professors: false,
      users: false,
      courses: false,
      reviews: false,
    })
  }

  return (
    <div>
      <NavBar
        onToggleComponent={onToggleComponent}
        onHome={onHome}
        componentStates={componentStates}
      />
      {componentStates.professors && <ProfessorsComponent />}
      {componentStates.users && <UsersComponent />}
      {componentStates.courses && <CoursesComponent />}
      {componentStates.reviews && <ReviewsComponent />}
    </div>
  )
}

export default Home
