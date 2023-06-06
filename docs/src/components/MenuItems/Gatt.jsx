import * as React from "react";
import ListSubheader from "@mui/material/ListSubheader";
import List from "@mui/material/List";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Collapse from "@mui/material/Collapse";
import InboxIcon from "@mui/icons-material/MoveToInbox";
import ExpandLess from "@mui/icons-material/ExpandLess";
import ExpandMore from "@mui/icons-material/ExpandMore";
import StarBorder from "@mui/icons-material/StarBorder";
import SubjectIcon from "@mui/icons-material/Subject";
import ArticleIcon from "@mui/icons-material/Article";
import { useRouter } from "next/router";
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
            sx={{ width: "100%", maxWidth: 320, bgcolor: "background.paper" }}
            component="nav"
            aria-labelledby="nested-list-subheader"
        >
            <ListItemButton sx={styleSx} onClick={() => setOpen(!open)}>
                <ListItemIcon>
                    <SubjectIcon sx={{ fontSize: 20, color: "#b33ce6" }} />
                </ListItemIcon>
                <ListItemText
                    sx={{ my: 0 }}
                    primary="Gatt"
                    primaryTypographyProps={{
                        fontSize: 16,
                        fontWeight: "bold",
                        letterSpacing: 0,
                    }}
                />
                {open ? <ExpandLess /> : <ExpandMore />}
            </ListItemButton>
            <Collapse in={open} timeout="auto" unmountOnExit>
                <List component="div1" disablePadding>
                    <ListItemButton
                        sx={styleSx}
                        className={
                            router.pathname === "/gatt/bonding" ? "bg-[#b33ce6]" : ""
                        }
                    >
                        <ListItemIcon></ListItemIcon>

                        <Link href="/gatt/bonding">
                            <ListItemText
                                primary="Bonding"
                                primaryTypographyProps={{
                                    fontSize: 12,
                                    letterSpacing: 0,
                                }}
                            />
                        </Link>
                    </ListItemButton>
                </List>
                
                <List component="div1" disablePadding>
                    <ListItemButton
                        sx={styleSx}
                        className={
                            router.pathname === "/gatt/connection" ? "bg-[#b33ce6]" : ""
                        }
                    >
                        <ListItemIcon></ListItemIcon>

                        <Link href="/gatt/connection">
                            <ListItemText
                                primary="Connection"
                                primaryTypographyProps={{
                                    fontSize: 12,
                                    letterSpacing: 0,
                                }}
                            />
                        </Link>
                    </ListItemButton>
                </List>

                <List component="div1" disablePadding>
                    <ListItemButton
                        sx={styleSx}
                        className={
                            router.pathname === "/gatt/device" ? "bg-[#b33ce6]" : ""
                        }
                    >
                        <ListItemIcon></ListItemIcon>

                        <Link href="/gatt/device">
                            <ListItemText
                                primary="Device Profile"
                                primaryTypographyProps={{
                                    fontSize: 12,
                                    letterSpacing: 0,
                                }}
                            />
                        </Link>
                    </ListItemButton>
                </List>
                <List component="div1" disablePadding>
                    <ListItemButton
                        sx={styleSx}
                        className={
                            router.pathname === "/gatt/discover" ? "bg-[#b33ce6]" : ""
                        }
                    >
                        <ListItemIcon></ListItemIcon>

                        <Link href="/gatt/discover">
                            <ListItemText
                                primary="Discover"
                                primaryTypographyProps={{
                                    fontSize: 12,
                                    letterSpacing: 0,
                                }}
                            />
                        </Link>
                    </ListItemButton>
                </List>

                
            </Collapse>
        </List>
    );
}
