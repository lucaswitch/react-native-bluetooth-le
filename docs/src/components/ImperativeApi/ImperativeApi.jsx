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

export default function ImperativeApi() {
  const [open, setOpen] = React.useState(true);

  const handleClick = () => {
    setOpen(!open);
  };

  return (
    <List
      sx={{ width: '100%', maxWidth: 360, bgcolor: 'background.paper' }}
      component="nav"
      aria-labelledby="nested-list-subheader"
    >
      <ListItemButton onClick={handleClick}>
        <ListItemIcon>
          <SubjectIcon />
        </ListItemIcon>
        <ListItemText primary="Imperative API" />
        {open ? <ExpandLess /> : <ExpandMore />}
      </ListItemButton>
      <Collapse in={open} timeout="auto" unmountOnExit>
        <List component="div1" disablePadding>
          <ListItemButton sx={{ pl: 2 }}>
            <ListItemIcon>
            </ListItemIcon>
            <ListItemText primary="Overview" />
          </ListItemButton>
        </List>
        <List component="div1" disablePadding>
          <ListItemButton sx={{ pl: 2 }}>
            <ListItemIcon>
            </ListItemIcon>
            <ListItemText primary="Overview" />
          </ListItemButton>
        </List>
        <List component="div1" disablePadding>
          <ListItemButton sx={{ pl: 2 }}>
            <ListItemIcon>
            </ListItemIcon>
            <ListItemText primary="Overview" />
          </ListItemButton>
        </List>
      </Collapse>
    </List>

  
    
  );
}