import React from "react";
import GS from "../content/GS.mdx"
import { Box, Toolbar, Typography } from "@mui/material";

const drawerWidth = 240;

export default function GettingStarted() {
  return (
    <div className="ml-60" class="prose prose-lg">
      <Box
        component="main"
        sx={{ flexGrow: 1, p: 3, position:"center" , width: { sm: `calc(100% - ${drawerWidth}px)` } }}
      >
        <Toolbar />
        <GS/>
      </Box>
    </div>
  );
}