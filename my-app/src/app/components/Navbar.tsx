import styles from "@/app/components/css/navbar.module.css";
import React, {useEffect, useState} from 'react'
import HomeIcon from '@mui/icons-material/Home';
import {ComponentStates} from "@/types/ComponentStates";

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
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const handleButtonClick = (component: keyof ComponentStates) => {
    // Log the componentStates object to the console for debugging
    console.log("Component States:", componentStates);

    // Trigger the onToggleComponent function with the specified component
    onToggleComponent(component);
  };
  
  useEffect(() => {
    const checkLoginStatus = () => {
        fetch(process.env.NEXT_PUBLIC_BASE_URL + '/hello')
        .then((response) => {
          setIsLoggedIn(response.ok);
        })
        .catch((error) => {
          console.error('Error checking login status', error);
          setIsLoggedIn(false);
        });
    };

    checkLoginStatus();
  }, []);

  const handleLoginClick = () => {
      window.location.href = `${process.env.NEXT_PUBLIC_BASE_URL}/${
          isLoggedIn ? "logout" : "login"
      }`;
    onToggleComponent("courseDashboard");
  };

  return (
    <nav className={styles.nav}>
      <div>
        <ul>
          <li>
            <a>
              <button onClick={onHome}>
                <HomeIcon />
              </button>
            </a>
          </li>
          <li>
            <a>
              <button onClick={handleLoginClick}>
                {isLoggedIn ? 'Logout' : 'Login'}
              </button>
            </a>
          </li>
          {/* <li>
            <a>
              <button onClick={() => handleButtonClick("courses")}>
                Course
              </button>
            </a>
          </li> */}
          {/* <li>
            <a>
              <button onClick={() => handleButtonClick("reviews")}>
                Review
              </button>
            </a>
          </li> */}
          <li>
            <a>
              <button onClick={() => handleButtonClick("user")}>My Profile</button>
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
