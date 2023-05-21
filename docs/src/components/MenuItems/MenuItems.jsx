import * as React from 'react';
import ListSubheader from '@mui/material/ListSubheader';
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Collapse from '@mui/material/Collapse';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import ExpandLess from '@mui/icons-material/ExpandLess';
import ExpandMore from '@mui/icons-material/ExpandMore';
import StarBorder from '@mui/icons-material/StarBorder';
import SubjectIcon from '@mui/icons-material/Subject';
import ArticleIcon from '@mui/icons-material/Article';
import {useRouter} from "next/router";
import Link from "next/link";

export default function GettingStarted() {
  const [open, setOpen] = React.useState(true);
  const router = useRouter();
  const styleSx = {
    "&:hover": {
      backgroundColor: "#b33ce6"
    }
  }
  return (
    <List
      sx={{ width: '100%', maxWidth: 320, bgcolor: 'background.paper' }}
      component="nav"
      aria-labelledby="nested-list-subheader"
    >
      <ListItemButton 
      sx={styleSx}
      onClick={() => setOpen(!open)}>
        <ListItemIcon>
          <SubjectIcon sx={{ fontSize: 20, color:'#b33ce6' }} />
        </ListItemIcon >
        <ListItemText
            sx={{ my: 0 }}
            primary="Start Here"
            primaryTypographyProps={{
                fontSize: 12,
                fontWeight: 'bold',
                letterSpacing: 0,
            }}
            />
        {open ? <ExpandLess /> : <ExpandMore />}
      </ListItemButton>
      <Collapse in={open} timeout="auto" unmountOnExit>

        <List component="div1" disablePadding>
          <ListItemButton className={router.pathname === "/gettingStarted" ? "bg-[#b33ce6]" : ""} >
            <ListItemIcon>
            </ListItemIcon>

            <Link href='/gettingStarted'>
              <ListItemText primary="Getting Started"
                            primaryTypographyProps={{
                              fontSize: 12,
                              fontWeight: 'bold',
                              letterSpacing: 0,}}
              />
            </Link>
          </ListItemButton>
        </List>

        <List component="div1" disablePadding>
          <ListItemButton className={router.pathname === "/installation" ? "bg-[#b33ce6]" : ""}>
            <ListItemIcon>
            </ListItemIcon>
            <Link href='/installation'>
              <ListItemText primary="Installation"
                            primaryTypographyProps={{
                              fontSize: 12,
                              fontWeight: 'bold',
                              letterSpacing: 0,}}
              />
            </Link>

          </ListItemButton>
        </List>

      </Collapse>
    </List>
  );
}