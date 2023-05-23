import React from "react";
import "prismjs/themes/prism-tomorrow.css";
import { MDXProvider } from "@mdx-js/react";
import '../styles/global.scss';
import NavBarItems from "@/components/NavBarItems/NavBarItems";
import SearchAppBar from "@/components/AppBar/AppBar";
import Blog from "@/components/Content/Blog";
import StickyFooter from "@/components/StickyFooter/StickyFooter";

const components = {
  h1: Blog
}

export default function App({ Component, pageProps }) {
  return (
    <MDXProvider components={components}>
      <NavBarItems>
        <SearchAppBar/>
      </NavBarItems>

      <Component {...pageProps} />
      {/*<Blog />*/}

      <StickyFooter />
      </MDXProvider>
  );
}
