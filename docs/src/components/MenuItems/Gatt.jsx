import * as React from "react";
import List from "@mui/material/List";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Collapse from "@mui/material/Collapse";
import ExpandLess from "@mui/icons-material/ExpandLess";
import ExpandMore from "@mui/icons-material/ExpandMore";
import SubjectIcon from "@mui/icons-material/Subject";
import {useRouter} from "next/router";
import Link from "next/link";

export default function Gatt() {
  const [open, setOpen] = React.useState(true);
  const router = useRouter();
  const styleSx = {
    "&:hover": {
      backgroundColor: "rgba(179,60,230,0.77)",
      color: "#FFFFFF !important",
    },
  };
  return (
    <List
      sx={{width: "100%", maxWidth: 320, bgcolor: "background.paper"}}
      component="nav"
      aria-labelledby="nested-list-subheader"
    >
      <ListItemButton sx={styleSx} onClick={() => setOpen(!open)}>
        <ListItemIcon>
          <SubjectIcon sx={{fontSize: 20, color: "#b33ce6"}}/>
        </ListItemIcon>
        <ListItemText
          sx={{my: 0}}
          primary="Gatt"
          primaryTypographyProps={{
            fontSize: 16,
            fontWeight: "bold",
            letterSpacing: 0,
          }}
        />
        {open ? <ExpandLess/> : <ExpandMore/>}
      </ListItemButton>
      <Collapse in={open} timeout="auto" unmountOnExit>
        <List component="div1" disablePadding>
          <Link href="/gatt/bonding">

            <ListItemButton
              sx={styleSx}
              className={
                router.pathname === "/gatt/bonding" ? "font-bold text-white bg-[#b33ce6]" : ""
              }
            >
              <ListItemIcon></ListItemIcon>

              <ListItemText
                primary="Bonding"
                primaryTypographyProps={{
                  fontSize: 12,
                  letterSpacing: 0,
                }}
              />
            </ListItemButton>
          </Link>

        </List>

        <List component="div1" disablePadding>
          <Link href="/gatt/connection">

            <ListItemButton sx={styleSx}
                            className={
                              router.pathname === "/gatt/connection" ? "font-bold text-white bg-[#b33ce6]" : ""
                            }
            >
              <ListItemIcon></ListItemIcon>

              <ListItemText
                primary="Connection"
                primaryTypographyProps={{
                  fontSize: 12,
                  letterSpacing: 0,
                }}
              />
            </ListItemButton>
          </Link>

        </List>

        <List component="div1" disablePadding>
          <Link href="/gatt/device">

          <ListItemButton
            sx={styleSx}
            className={
              router.pathname === "/gatt/device" ? "font-bold text-white bg-[#b33ce6]" : ""
            }
          >
            <ListItemIcon></ListItemIcon>

              <ListItemText
                primary="Device Profile"
                primaryTypographyProps={{
                  fontSize: 12,
                  letterSpacing: 0,
                }}
              />
          </ListItemButton>
          </Link>

        </List>
        <List component="div1" disablePadding>
          <Link href="/gatt/discover">

          <ListItemButton
            sx={styleSx}
            className={
              router.pathname === "/gatt/discover" ? "font-bold text-white bg-[#b33ce6]" : ""
            }
          >
            <ListItemIcon></ListItemIcon>

              <ListItemText
                primary="Discover"
                primaryTypographyProps={{
                  fontSize: 12,
                  letterSpacing: 0,
                }}
              />
          </ListItemButton>
          </Link>

        </List>


      </Collapse>
    </List>
  );
}
