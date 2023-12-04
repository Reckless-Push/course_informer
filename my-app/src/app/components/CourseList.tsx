import React, {ChangeEvent, FC, InputHTMLAttributes, useState} from 'react'
import CourseCard from './CourseCard';
import {courseData} from '../../courseData';

const CourseList = () => {
    return(
        <div>
        {courseData.map(course => (
          <CourseCard key={course.title} csID = {course.csID} title={course.title} semester={course.semester} />
        ))}
      </div>
  
    );
}

export default CourseList;