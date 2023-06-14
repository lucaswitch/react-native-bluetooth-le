import React from "react";
import GS from "../content/GS.mdx"
import { Box, Toolbar} from "@mui/material";
import TrackingBar from "@/components/TrackingBar";

export default function GettingStarted() {
  return (
    <div className="mx-auto prose prose-2xl">
      <div className="flex justify-between w-full ml-24 pr-64">
        <div className="flex-row ">
          <Box
            component="main"
            sx={{ flexGrow: 1, p: 3, position:"center" }}
          >
            <Toolbar />
            <GS/>
          </Box>
        </div>
        <div >
          <TrackingBar topicsTitles={[
            'Introduction',
            'Compatibility',
            'Getting started'
          ]}/>
        </div>
      </div>
    </div>
  );
}