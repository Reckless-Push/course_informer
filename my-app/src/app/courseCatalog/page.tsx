"use client"
import React from 'react'
import './courseCatalog.css';
import CourseList from '../components/CourseList';
import CourseFiltersList from '../components/CourseFiltersList';
import { ComponentStates } from '@/types/ComponentStates'

interface courseCatalogPage {
  onToggleComponent: (component: keyof ComponentStates) => void
  onHome: () => void
  componentStates: {
      courses: boolean
      reviews: boolean
      courseDashboard: boolean,
      login: boolean
  }
}


function courseCatalogPage({onToggleComponent,onHome,componentStates,}: courseCatalogPage){
  return (
    <div className='container'> 
      <div className='filters_column'>
        <CourseFiltersList />
      </div>
      <div className='list_column'>
        <CourseList/>
      </div>
    </div>
  )
}

export default courseCatalogPage
