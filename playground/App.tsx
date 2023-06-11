import {SafeAreaView, StatusBar} from "react-native";
import {BluetoothState, Bonding, Discovery} from "./src/components/demo";

function App(): JSX.Element {
    return (
        <SafeAreaView style={{flex: 1, backgroundColor: '#EAEAEA', padding: 6, gap: 10}}>
            <StatusBar backgroundColor="#EAEAEA" barStyle="dark-content"/>
            <BluetoothState/>
            {/*<Bonding/>
            <Discovery/>*/}
        </SafeAreaView>
    );
}

export default App;
