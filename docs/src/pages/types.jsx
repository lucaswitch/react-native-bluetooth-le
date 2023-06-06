import React from 'react'
import {Box, Toolbar} from "@mui/material";
import Type from "@/content/Type.mdx";
import BluetoohState from "@/content/BluetoohAdapter.mdx";

export default function Types() {
  const drawerWidth = 240;

  return (
    <div className="ml-60 prose prose-2xl">
      <div className="flex ml-24 pr-64">
        <div className="flex-row ">
          <Box
            component="main"
            sx={{ flexGrow: 1, p: 3, position:"center" }}
          >
            <Toolbar />
            <Type />
          </Box>
        </div>
      </div>
    </div>
  );
}