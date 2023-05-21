import * as React from "react";
import FormatAlignLeftIcon from '@mui/icons-material/FormatAlignLeft';
import FormatAlignCenterIcon from '@mui/icons-material/FormatAlignCenter';
import FormatAlignRightIcon from '@mui/icons-material/FormatAlignRight';
import FormatBoldIcon from '@mui/icons-material/FormatBold';
import FormatItalicIcon from '@mui/icons-material/FormatItalic';
import Box from '@mui/material/Box';
import Divider from '@mui/material/Divider';
import {Button} from '@mui/material';
import ListItemText from "@mui/material/ListItemText";
import Link from "next/link";

export default function Logo() {
  return (
    <div>
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          width: 'fit-content',
          borderRadius: 1,
          bgcolor: 'background.paper',
          color: 'text.secondary',
          '& svg': {
            m: 1.5,
          },
          '& hr': {
            mx: 0.5,
          },
        }}
      >
        <img src='/images/RN-logo.png' width={50} alt="Logo da biblioteca RN-Bluetooth"/>
        <Divider orientation="vertical" variant="middle" flexItem style={{marginLeft: 15}}/>
        <Link href='/'>
          <Button variant="text" style={{color: '#b33ce6'}}>RN-Bluetooth</Button>
        </Link>

      </Box>
    </div>
  );
}