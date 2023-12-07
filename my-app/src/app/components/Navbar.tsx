"use client";

import styles from "@/app/components/css/navbar.module.css";

import { ComponentStates } from "@/types/ComponentStates";

export interface NavBarProps {
  onToggleComponent: (component: keyof ComponentStates) => void;
  onHome: () => void;
  componentStates: {
    courses: boolean;
    reviews: boolean;
    user: boolean;
    courseDashboard: boolean;
    login: boolean;
  };
}

function Navbar({ onToggleComponent, onHome, componentStates }: NavBarProps) {
  const handleButtonClick = (component: keyof ComponentStates) => {
    // Log the componentStates object to the console for debugging
    console.log("Component States:", componentStates);

    // Trigger the onToggleComponent function with the specified component
    onToggleComponent(component);
  };
  return (
    <nav className={styles.nav}>
      <div>
        <ul>
          <li>
            <a>
              <button onClick={() => handleButtonClick("courses")}>
                Course
              </button>
            </a>
          </li>
          <li>
            <a>
              <button onClick={() => handleButtonClick("reviews")}>
                Review
              </button>
            </a>
          </li>
          <li>
            <a>
              <button onClick={() => handleButtonClick("user")}>User</button>
            </a>
          </li>
          <li>
            <a>
              <button onClick={() => handleButtonClick("courseDashboard")}>
                View All Courses
              </button>
            </a>
          </li>

          {/* <li><a>My Profile</a></li> */}
          {/* <li><a>Logout</a></li> */}
        </ul>
      </div>
    </nav>
  );
}

export default Navbar;
