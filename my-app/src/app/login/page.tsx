"use client";
import React, { useState } from 'react';
import './login.css';
import InputField from '../components/InputField';
import GoogleSignInButton from '../components/GoogleSignInButton';

const LoginPage = () => {
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
        <GoogleSignInButton />
      </form>
    </div>
  );
};

export default LoginPage;
