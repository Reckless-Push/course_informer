import React from 'react'
import CourseFilter from './CourseFilter';
import SearchBox from './SearchBox';

const CourseFiltersList = () => {
  return (
    <div>
        <SearchBox />
        <CourseFilter />
        <CourseFilter />
        <CourseFilter />
    </div>
  )
}

export default CourseFiltersList;
