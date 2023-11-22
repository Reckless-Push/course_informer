"use client"
import React from 'react'
import './courseCatalog.css';
import CourseList from '../components/CourseList';
import CourseFilter from '../components/CourseFilter';


const courseCatalogPage = () => {
  return (
    <div className='container'> 
      <div className='filters_column'>
        <CourseFilter />
      </div>
      <div className='list_column'>
        <CourseList/>
      </div>

    </div>
  )
}

export default courseCatalogPage
