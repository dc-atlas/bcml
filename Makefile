.PHONY: all checksbgn graphmlconvert exporter filter annotator copy-jars setup-jar

TESTFLAGS=-Dmaven.test.skip=true
INSTALLED_JAR_DIR=$(CURDIR)/bin

EXPORTER_JAR=exporter-1.0-SNAPSHOT-jar-with-dependencies.jar
ANNOTATOR_JAR=annotator-1.0-SNAPSHOT-jar-with-dependencies.jar
FILTER_JAR=filter-1.0-SNAPSHOT-jar-with-dependencies.jar
CONVERTER_JAR=graphmlconvert-1.0-SNAPSHOT-jar-with-dependencies.jar
CHECKER_JAR=checksbgn-1.0-SNAPSHOT-jar-with-dependencies.jar

all: prep checksbgn graphmlconvert exporter filter annotator copy-jars setup-jar

prep:
	echo "Preparing..."
	mvn clean install -Dmaven.test.skip=true

checksbgn:
	echo "Building checksbgn"
	cd ./checksbgn;	mvn assembly:assembly $(TESTFLAGS)

graphmlconvert:
	echo "Building graphmlconvert"
	cd ./graphmlconvert; mvn assembly:assembly $(TESTFLAGS)

exporter:
	cd ./exporter; mvn assembly:assembly $(TESTFLAGS)

filter: 
	cd ./filter; mvn assembly:assembly $(TESTFLAGS)

annotator:
	cd ./annotator; mvn assembly:assembly $(TESTFLAGS)

copy-jars:
	find ./ -path './bin' -prune -o -name \*-with-dependencies.jar -exec cp {} bin/ \;

setup-jar:
	cd bin; sed -i.bak 's?^JAR_LOCATION="*."?JAR_LOCATION=""?1' *.sh
	cd bin; sed -i 's?JAR_LOCATION="*."?JAR_LOCATION="$(INSTALLED_JAR_DIR)"?1' *.sh
	cd bin; sed -i 's?\(JAR_LOCATION=\)"\(.*\)"?\1"\2/$(EXPORTER_JAR)"?1' bcml_export.sh
	cd bin; sed -i 's?\(JAR_LOCATION=\)"\(.*\)"?\1"\2/$(ANNOTATOR_JAR)"?1' bcml_annotate.sh
	cd bin; sed -i 's?\(JAR_LOCATION=\)"\(.*\)"?\1"\2/$(CONVERTER_JAR)"?1' bcml2graphml.sh
	cd bin; sed -i 's?\(JAR_LOCATION=\)"\(.*\)"?\1"\2/$(CHECKER_JAR)"?1' bcml_check.sh
	cd bin; sed -i 's?\(JAR_LOCATION=\)"\(.*\)"?\1"\2/$(FILTER_JAR)"?1' bcml_filter.sh
	cd bin; rm -f *.bak

