"use client";

import styles from "@/app/components/css/navbar.module.css";

import { ComponentStates } from '@/types/ComponentStates'

interface NavBarProps {
    onToggleComponent: (component: keyof ComponentStates) => void
    onHome: () => void
    componentStates: {
        courses: boolean
        reviews: boolean
    }
}

function Navbar({
    onToggleComponent,
    onHome,
    componentStates,
}: NavBarProps) {

    return (

        <nav className={styles.nav}>
            <div>
                <ul>
                    <li><a><button onClick={() => onToggleComponent('courses')}>Course</button></a></li>
                    <li><a><button onClick={() => onToggleComponent('reviews')}>Review</button></a></li>
                    {/* <li><a>My Profile</a></li> */}
                    {/* <li><a>Logout</a></li> */}
                </ul>
            </div>

        </nav>
    );
}

export default Navbar;