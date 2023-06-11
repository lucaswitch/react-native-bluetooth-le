import React from "react";
import "prismjs/themes/prism-tomorrow.css";
import { MDXProvider } from "@mdx-js/react";
import '../styles/global.scss';
import NavBarItems from "@/components/NavBarItems/NavBarItems";

export default function App({ Component, pageProps }) {
  return (
    <MDXProvider>
      <NavBarItems/>
      <Component {...pageProps} />
    </MDXProvider>
  );
}
