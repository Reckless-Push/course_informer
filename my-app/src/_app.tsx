import React from "react";
import "../styles/globals.css";
import "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css";
import type { AppProps } from "next/app";
import Head from "next/head";

function MyApp({ Component, pageProps }: AppProps) {
  return (
    <>
      <Head>
        <title>CICS Course Informer</title>
      </Head>
      <Component {...pageProps} />
    </>
  );
}

export default MyApp;
