.PHONY: all checksbgn graphmlconvert exporter filter annotator

TESTFLAGS=-Dmaven.test.skip=true

all: prep checksbgn graphmlconvert exporter filter annotator

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
	cf ./annotator; mvn assembly:assembly $(TESTFLAGS)
