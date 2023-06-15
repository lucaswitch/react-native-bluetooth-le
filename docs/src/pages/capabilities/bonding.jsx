import React from "react";
import Bonding from "../../content/capabilities/Bonding.mdx"
import {Box, Toolbar} from "@mui/material";
import TrackingBar from "@/components/TrackingBar";

export default function BondingButton() {
  return (
    <div className="mx-auto prose prose-2xl">
      <div className="flex justify-between w-full ml-24 pr-64">
        <div className="flex-row ">
          <Box
            component="main"
            sx={{flexGrow: 1, p: 3, position: "center"}}
          >
            <Toolbar/>
            <Bonding/>
          </Box>
        </div>
        <div>
          <TrackingBar topicsTitles={[
            'Bonding Button',
            'Bonding'
          ]}/>
        </div>
      </div>
    </div>
  );
}