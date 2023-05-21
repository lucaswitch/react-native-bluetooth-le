import React from "react";

export default function Installation(){
  return(
    <div className="ml-60">
      <div className="flex ml-24 pr-64">
        <div className="flex-row ">
          <p className="text-4xl font-bold mb-10">Installation</p>
          <p>
            <b>react-native-bluetooth-le</b> library stands for implement commons bluetooth low energy specifications in
            react-native ecosystem.
            It uses the new react-native turbo module architecture thats powers a better performance for react-native
            applications. Exposes a bluetooth javascript api with easy subscribe/unsubscribe pattern approach to handle bluetooth
            events and provides foreground mode transmission on android.

          </p>
          {/*<p className="text-2xl font-bold mt-10 mb-4">Compatibility</p>*/}
          {/*<p>*/}
          {/*  - Must be a react-native 0.69 or above. <br/>*/}
          {/*  - Must be hermes engine enabled. please checkout the link: <a*/}
          {/*  href="https://reactnative.dev/docs/new-architecture-intro">https://reactnative.dev/docs/new-architecture-intro</a> to*/}
          {/*  enable. <br/>*/}
          {/*  <br/>*/}
          {/*  - Android (Fully supported on sdk 28 or above). <br/>*/}
          {/*  - IOs (Not yet supported)*/}
          {/*</p>*/}
        </div>
      </div>
    </div>
  );
}