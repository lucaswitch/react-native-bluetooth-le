cd ../;
yarn remove ../lib;
yarn add ../lib;
cd android;
./gradlew generateCodegenArtifactsFromSchema;
cd ..;
