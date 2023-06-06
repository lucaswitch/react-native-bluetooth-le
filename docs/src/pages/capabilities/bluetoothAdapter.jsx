import React from "react";
import BluetoohAdapter from "../../content/capabilities/BluetoohAdapter.mdx"
import {Box, Toolbar} from "@mui/material";
import GS from "@/content/GS.mdx";


export default function BluetoothAdapters(){
  const drawerWidth = 240;

  return(
    <div className="ml-60 prose prose-2xl">
      <div className="flex ml-24 pr-64">
        <div className="flex-row ">
          <Box
            component="main"
            sx={{ flexGrow: 1, p: 3, position:"center" }}
          >
            <Toolbar />
            <BluetoohAdapter />

          </Box>
        </div>
      </div>
    </div>
  );
}