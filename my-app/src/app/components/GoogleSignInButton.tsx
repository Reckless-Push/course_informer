import React from "react";
import styles from "./css/googleSignInButton.module.css";
import { ComponentStates } from "@/types/ComponentStates";

interface GoogleSignInProps {
  onToggleComponent: (component: keyof ComponentStates) => void;
  onHome: () => void;
  componentStates: {
    courses: boolean;
    reviews: boolean;
    courseDashboard: boolean;
    login: boolean;
  };

}

function GoogleSignInButton({
  onToggleComponent,
  onHome,
  componentStates,
}: GoogleSignInProps){
  const handleClick = () => {
    console.log("Clicked Google Sign-In Button");
  };

  return (
    <div id="google-signin-button" className={styles['google-button']} onClick={handleClick}>
      <div className={styles['google-icon-wrapper']}>
        <img
          className={styles['google-icon']}
          src="https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg"
          alt="Google logo"
        />
      </div>
      <span className={styles['button-text']}>Sign in with your Google Account</span>
    </div>
  );
};

export default GoogleSignInButton;
