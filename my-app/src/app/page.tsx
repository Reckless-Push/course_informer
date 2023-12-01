'use client'
import React, { useState } from 'react'
import CoursePage from '@/app/course/page'
import Review from '@/app/review/page'
import { ComponentStates } from '@/types/ComponentStates'
import Navbar from '@/app/components/Navbar'
type ComponentKey = 'courses' | 'reviews'

const Home = () => {
    const [componentStates, setComponentStates] = useState<ComponentStates>({
        courses: false,
        reviews: false,
    })

    const onToggleComponent = (component: ComponentKey) => {
        setComponentStates((prev) => ({ ...prev, [component]: !prev[component] }))
    }

    const onHome = () => {
        setComponentStates({
            courses: false,
            reviews: false,
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
            {componentStates.courses && <CoursePage />}
            {componentStates.reviews && <Review />}
        </div>
    )
}

export default Home
