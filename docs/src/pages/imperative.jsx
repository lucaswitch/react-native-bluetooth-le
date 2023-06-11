import React from "react";
import Imper from "../content/Imper.mdx"
import { Box, Toolbar} from "@mui/material";
import TrackingBar from "@/components/TrackingBar";

export default function Imperative() {
  return (
    <div className="mx-auto prose prose-2xl">
      <div className="flex justify-between w-full ml-24 pr-64">
        <div className="flex-row ">
          <Box
            component="main"
            sx={{ flexGrow: 1, p: 3, position:"center" }}
          >
            <Toolbar />
            <Imper/>
          </Box>
        </div>
        <div >
          <TrackingBar topicsTitles={[
            'Imperative API',
          ]}/>
        </div>
      </div>
    </div>
  );
}