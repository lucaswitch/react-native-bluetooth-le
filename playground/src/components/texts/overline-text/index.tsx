import {ReactNode} from "react";
import {Container} from "./style";
import {StyleProp} from "react-native";

export function OverlineText({children, style}: { children: ReactNode, style?: StyleProp<object> }) {
    return <Container style={style}>
        {children}
    </Container>
}
