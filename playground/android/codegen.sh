cd ../;
yarn add ../lib/ --save;
cd android;
./gradlew generateCodegenArtifactsFromSchema;
cd ..;
