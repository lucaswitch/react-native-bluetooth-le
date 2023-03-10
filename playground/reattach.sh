rm -r node_modules/react-native-bluetooth-le;
yarn add ../lib;
cd android;
./gradlew clean;
./gradlew generateCodegenArtifactsFromSchema;
cd ..;
