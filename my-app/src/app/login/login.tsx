"use client";
import React, { useState } from 'react';
import './login.css';
import InputField from '../components/InputField';
import GoogleSignInButton from '../components/GoogleSignInButton';
import { ComponentStates } from "@/types/ComponentStates";

interface LoginPage {
  onToggleComponent: (component: keyof ComponentStates) => void;
  onHome: () => void;
  componentStates: {
    courses: boolean;
    reviews: boolean;
    courseDashboard: boolean;
    login: boolean;
    user: boolean;
  };
}

function LoginPage({onToggleComponent,
  onHome,
  componentStates}: LoginPage){
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  return (
    <div className="container"> {/* Apply the container class */}
      <h1>UMASS COURSE INFORMER</h1>
      <form>
        <InputField
          type="text"
          placeholder="username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <InputField
          type="password"
          placeholder="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <GoogleSignInButton  onToggleComponent={onToggleComponent}
          onHome={onHome}
          componentStates={componentStates}/>
      </form>
    </div>
  );
};

export default LoginPage;
