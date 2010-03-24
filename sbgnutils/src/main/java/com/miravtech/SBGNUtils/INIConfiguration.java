package com.miravtech.SBGNUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class INIConfiguration {

	static public Properties getConfiguration() {
		Properties p = new Properties();
		String homeDir = ".";
		if (System.getenv("HOME") != null) {
			homeDir = System.getenv("HOME");
		} else {
			if (System.getenv("USERPROFILE") != null) {
				homeDir = System.getenv("USERPROFILE");
			} else {
				System.err.println("User directory cannot be identified, looking for SBGN.ini in the current directory.");
			}
		}
		try {
			p.load(new FileInputStream(new File(homeDir,"SBGN.ini")));
		} catch (Exception e) {
			// ignore the exception
			System.err.println("Warning, SBGN.ini cannot be loaded");
		}
		return p;

	}
}
