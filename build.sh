#!/bin/sh

mvn clean install
cd checksbgn
mvn assembly:assembly

cd ..
cd graphmlconvert
mvn assembly:assembly
cd ..


cd exporter
mvn assembly:assembly
cd ..


cd filter
mvn assembly:assembly
cd ..


cp checksbgn/target/checksbgn-1.0-SNAPSHOT-jar-with-dependencies.jar .
cp graphmlconvert/target/graphmlconvert-1.0-SNAPSHOT-jar-with-dependencies.jar .
cp exporter/target/exporter-1.0-SNAPSHOT-jar-with-dependencies.jar .
cp filter/target/filter-1.0-SNAPSHOT-jar-with-dependencies.jar .
