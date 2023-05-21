import * as React from 'react';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import CssBaseline from '@mui/material/CssBaseline';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import List from '@mui/material/List';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import MailIcon from '@mui/icons-material/Mail';
import { Button } from '@mui/material';
import MenuItems from '../MenuItems/MenuItems';
import Logo from '../Logo/Logo';
import Capabilities from '../MenuItems/MenuItems2';
import { BorderAll } from '@mui/icons-material';
import SearchAppBar from '../AppBar/AppBar';

const drawerWidth = 220;

export default function NavBarItems() {
  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
      <SearchAppBar
        elevation={0}
        position="fixed"
        sx={{ width: `calc(100% - ${drawerWidth}px)`, ml: `${drawerWidth}px`, backgroundColor:'white'}}
      >
        <Toolbar>
          <Typography variant="h6" noWrap component="div">
            Permanent drawer
          </Typography>
        </Toolbar>
      </SearchAppBar>
      <Drawer
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          '& .MuiDrawer-paper': {
            width: drawerWidth,
            boxSizing: 'border-box',
            color:'black',
          },
        }}
        variant="permanent"
        anchor="left"
      >
        <Toolbar>
          <Logo/>
        </Toolbar>
        <Divider />
        <MenuItems/>
        <Capabilities/>
      </Drawer>
      <Box
        component="main"
        sx={{ flexGrow: 1, bgcolor: 'background.default', p: 3 }}
      >
        <Toolbar />
      </Box>
    </Box>
  );
}
