import React from "react";
import "../styles/globals.css";
import "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css";
import type { AppProps } from "next/app";

function MyApp({ Component, pageProps }: AppProps) {
  return (
    <>
      <Component {...pageProps} />
    </>
  );
}

export default MyApp;
