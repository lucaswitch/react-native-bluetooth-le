import * as React from 'react';
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Collapse from '@mui/material/Collapse';
import ExpandLess from '@mui/icons-material/ExpandLess';
import ExpandMore from '@mui/icons-material/ExpandMore';
import SubjectIcon from '@mui/icons-material/Subject';
import Link from "next/link";
import {useRouter} from "next/router";

export default function Capabilities() {
  const [open, setOpen] = React.useState(true);
  const router = useRouter();

  return (
    <List
      sx={{ width: '100%', maxWidth: 320, bgcolor: 'background.paper' }}
      component="nav"
      aria-labelledby="nested-list-subheader"
    >
      <ListItemButton onClick={() => setOpen(!open)}>
        <ListItemIcon>
          <SubjectIcon sx={{ fontSize: 20, color:'#b33ce6'}} />
        </ListItemIcon >
        <ListItemText
            sx={{ my: 0 }}
            primary="Capabilities"
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
          <ListItemButton className={router.pathname === "/bluetoothAdapter" ? "bg-[#b33ce6]" : ""}>
            <ListItemIcon>
            </ListItemIcon>
            <Link href='/bluetoothAdapter'>
              <ListItemText primary="Bluetooth Adapter"
                            primaryTypographyProps={{
                              fontSize: 12,
                              fontWeight: 'bold',
                              letterSpacing: 0,}}
              />
            </Link>

          </ListItemButton>
        </List>

        <List component="div1" disablePadding>
          <ListItemButton className={router.pathname === "/bluetoothState" ? "bg-[#b33ce6]" : ""}>
            <ListItemIcon>
            </ListItemIcon>
            <Link href='/bluetoothState'>
              <ListItemText primary="Bluetooth State"
                            primaryTypographyProps={{
                              fontSize: 12,
                              fontWeight: 'bold',
                              letterSpacing: 0,}}
              />
            </Link>

          </ListItemButton>
        </List>

        <List component="div1" disablePadding>
          <ListItemButton className={router.pathname === "/bonding" ? "bg-[#b33ce6]" : ""}>
            <ListItemIcon>
            </ListItemIcon>
            <Link href='/bonding'>
              <ListItemText primary="Bonding Button"
                            primaryTypographyProps={{
                              fontSize: 12,
                              fontWeight: 'bold',
                              letterSpacing: 0,}}
              />
            </Link>

          </ListItemButton>
        </List>

        <List component="div1" disablePadding>
          <ListItemButton className={router.pathname === "/discovery" ? "bg-[#b33ce6]" : ""}>
            <ListItemIcon>
            </ListItemIcon>
            <Link href='/discovery'>
              <ListItemText primary="Discovery"
                            primaryTypographyProps={{
                              fontSize: 12,
                              fontWeight: 'bold',
                              letterSpacing: 0,}}
              />
            </Link>

          </ListItemButton>
        </List>

        <List component="div1" disablePadding>
          <ListItemButton className={router.pathname === "/types" ? "bg-[#b33ce6]" : ""}>
            <ListItemIcon>
            </ListItemIcon>
            <Link href='/types'>
              <ListItemText primary="Types"
                            primaryTypographyProps={{
                              fontSize: 12,
                              fontWeight: 'bold',
                              letterSpacing: 0,}}
              />
            </Link>

          </ListItemButton>
        </List>

        <List component="div1" disablePadding>
          <ListItemButton className={router.pathname === "/gatt" ? "bg-[#b33ce6]" : ""}>
            <ListItemIcon>
            </ListItemIcon>
            <Link href='/gatt'>
              <ListItemText primary="GATT"
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