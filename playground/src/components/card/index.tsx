import {Container} from "./style";
import { ReactNode} from "react";

export function Card({children}: { children: ReactNode }) {
    return <Container>
        {children}
    </Container>
}
