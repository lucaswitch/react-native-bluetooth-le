import * as React from 'react'
import { 
    Box,
    Drawer,
    CssBaseline,
    AppBar,
    Toolbar,
    List,
    Typography,
    Divider,
    ListItem,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    Button
} from '@mui/material'
import InboxIcon from '@mui/icons-material/MoveToInbox';
import MailIcon from '@mui/icons-material/Mail';
import GettingStarted from '../GettingStarted/GettingStarted';
import ImperativeApi from '../ImperativeApi/ImperativeApi';


const drawerWidth = 260;

export function MenuLateral(){
    return (
        <Box sx={{ display: 'flex', bgcolor: 'background.paper' }}>
      <CssBaseline />
     <AppBar variant='contained' sx={{bgcolor: 'background.paper' }}>
        <Toolbar>
          
        </Toolbar>
     </AppBar>
      <Drawer 
      
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          '& .MuiDrawer-paper': {
            width: drawerWidth,
            boxSizing: 'content-box',
          },
        }}
        variant="permanent"
        anchor="left"
      >
        <Toolbar >
        <img src='/images/RN-logo.png' />
        <Divider orientation="vertical" variant="middle" flexItem />
        <Button variant="text" style={{color: '#b33ce6'}}>RN-Bluetooth</Button>
        </Toolbar>
        <Divider />
        <GettingStarted/>
        <ImperativeApi/>
      </Drawer>
      <Box
        component="main"
        sx={{ flexGrow: 1, bgcolor: 'background.default', p: 3 }}
      >
        <Toolbar />
      </Box>
    </Box>
    )
};