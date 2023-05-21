import React from "react";
import '../styles/global.scss';
import NavBarItems from "@/components/NavBarItems/NavBarItems";
import SearchAppBar from "@/components/AppBar/AppBar";
import Blog from "@/components/Content/Blog";
import StickyFooter from "@/components/StickyFooter/StickyFooter";

export default function App({ Component, pageProps }) {
  return (
    <>
      <NavBarItems>
        <SearchAppBar/>
      </NavBarItems>

      <Component {...pageProps} />
      {/*<Blog />*/}

      <StickyFooter />
    </>
  );
}
