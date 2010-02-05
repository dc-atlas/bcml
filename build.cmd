call c:\apache-maven-2.2.1\bin\mvn clean install

cd checksbgn

call c:\apache-maven-2.2.1\bin\mvn assembly:assembly

cd ..

cd graphmlconvert

call c:\apache-maven-2.2.1\bin\mvn assembly:assembly


cd ..

copy checksbgn\target\checksbgn-1.0-SNAPSHOT-jar-with-dependencies.jar .


copy graphmlconvert\target\graphmlconvert-1.0-SNAPSHOT-jar-with-dependencies.jar .


