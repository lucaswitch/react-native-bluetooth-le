import React from "react";
import Bonding from "../../content/capabilities/Bonding.mdx"
import { Box, Toolbar} from "@mui/material";

export default function BondingButton() {
  return (
    <div className="ml-60 prose prose-2xl">
      <div className="flex ml-24 pr-64">
        <div className="flex-row ">
          <Box
            component="main"
            sx={{ flexGrow: 1, p: 3, position:"center" }}
          >
            <Toolbar />
            <Bonding/>
          </Box>
        </div>
      </div>
    </div>
  );
}