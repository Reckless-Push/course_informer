'use client'
import React, { useState } from 'react'
import CoursePage from '@/app/course/course'
import CourseCatalogPage from '@/app/courseCatalog/page'
import { ComponentStates } from '@/types/ComponentStates'
import Navbar from '@/app/components/Navbar'
import ResponseForm from '@/app/review/review'
type ComponentKey = 'courses' | 'reviews' | 'courseDashboard' | 'login'

const Home = () => {
    const [componentStates, setComponentStates] = useState<ComponentStates>({
        courses: false,
        reviews: false,
        courseDashboard: false,
        login: false
    })

    const onToggleComponent = (component: ComponentKey) => {
        setComponentStates((prev) => ({ ...prev, [component]: !prev[component] }))
    }

    const onHome = () => {
        setComponentStates({
            courses: false,
            reviews: false,
            courseDashboard: false,
            login: false
        })
    }

    return (
        <div>
            <Navbar
                onToggleComponent={onToggleComponent}
                onHome={onHome}
                componentStates={componentStates}
            />

            {/* <Navbar></Navbar> */}
            {
            componentStates.courses && 
            <CoursePage onToggleComponent={onToggleComponent} onHome={onHome} componentStates={componentStates} />
            }

            {
            componentStates.reviews && 
            <ResponseForm onToggleComponent={onToggleComponent} onHome={onHome}componentStates={componentStates} />
            }

            {
            componentStates.courseDashboard && 
            <CourseCatalogPage onToggleComponent={onToggleComponent} onHome={onHome} componentStates={componentStates} />
            }
        </div>
    )
}

export default Home
