import React from "react";
import BluetoohAdapter from "../../content/capabilities/BluetoohAdapter.mdx"
import {Box, Toolbar} from "@mui/material";
import TrackingBar from "@/components/TrackingBar";

export default function BluetoothAdapters(){
  return(
  <div className="mx-auto prose prose-2xl">
    <div className="flex justify-between w-full ml-24 pr-64">
      <div className="flex-row">
        <Box
          component="main"
          sx={{ flexGrow: 1, p: 3, position:"center" }}
        >
          <Toolbar />
          <BluetoohAdapter />

        </Box>
      </div>
      <div className='ml-20'>
        <TrackingBar topicsTitles={[
          'Whether bluetooth adapter is enabled',
          'Gets whether bluetooth adapter has low energy support',
          'Gets bluetooth adapter name and address',
          'Gets whether location is enabled',
          'Listen to bluetooth adapter sudden state change',
          'Listen bluetooth discovery and find nearby devices'
        ]}/>
      </div>
    </div>
  </div>
  );
}