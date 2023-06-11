import React from 'react'
import {Box, Toolbar} from "@mui/material";
import Type from "../../content/capabilities/Type.mdx";
import TrackingBar from "@/components/TrackingBar";

export default function Types() {
  return (
    <div className="mx-auto prose prose-2xl">
      <div className="flex justify-between w-full ml-24 pr-64">
        <div className="flex-row ">
          <Box
            component="main"
            sx={{flexGrow: 1, p: 3, position: "center"}}
          >
            <Toolbar/>
            <Type/>

          </Box>
        </div>
        <div>
          <TrackingBar topicsTitles={[
            'Types',
          ]}/>
        </div>
      </div>
    </div>
  );
}