cd ../;
yarn add ../lib/;
cd android;
./gradlew generateCodegenArtifactsFromSchema;
cd ..;
yarn;
