package com.miravtech.annotator;

import java.io.FileInputStream;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String sbgnFileSource;
		String textFileSource;
		String sbgnFileDest;
		String algorithm;
		String varName;
		if (args.length > 4) {
			sbgnFileSource = args[0];
			textFileSource = args[1];
			sbgnFileDest = args[2];
			algorithm = args[3];
			varName = args[4];
		} else 
			throw new Exception("Please provide the following arguments: sbgn file source, text file source, sbgn file destination, algorithm, variable_name");
		
		SymbolList sl = new SymbolList(new FileInputStream(textFileSource));

	}

}
