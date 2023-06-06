import React from "react";
import Discoveries from "../../content/capabilities/Discoveries.mdx"
import { Box, Toolbar} from "@mui/material";

export default function Discovery() {
  return (
    <div className="ml-60 prose prose-2xl">
      <div className="flex ml-24 pr-64">
        <div className="flex-row ">
          <Box
            component="main"
            sx={{ flexGrow: 1, p: 3, position:"center" }}
          >
            <Toolbar />
            <Discoveries/>
          </Box>
        </div>
      </div>
    </div>
  );
}