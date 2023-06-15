import React from "react";
import { Box, Toolbar } from "@mui/material";
import {useRouter} from "next/router";

export default function GettingStarted() {
  const router = useRouter();

  React.useEffect(() => {
    if (router.pathname === '/')  router.push('/gettingStarted');
  },[])

  return (
    <></>
    // <div className="ml-60" class="prose">
    //   <Box
    //     component="main"
    //     sx={{ flexGrow: 1, p: 3, position:"center" , width: { sm: `calc(100% - ${drawerWidth}px)` } }}
    //   >
    //     <Toolbar />
    //
    //   </Box>
    // </div>
  );
}