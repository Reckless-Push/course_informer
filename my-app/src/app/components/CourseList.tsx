import React, {ChangeEvent, FC, InputHTMLAttributes, useState} from 'react'
import CourseCard from './CourseCard';

const CourseList = () => {
    return(
        <div>
            <CourseCard />
            <CourseCard />
            <CourseCard />
            <CourseCard />
            <CourseCard />
        </div>
    );
}

export default CourseList;