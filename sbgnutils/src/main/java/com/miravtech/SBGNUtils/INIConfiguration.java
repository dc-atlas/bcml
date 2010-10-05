/**
 *
 * Copyright (C) 2010 Razvan Popovici <rp@miravtech.com>
 * Copyright (C) 2010 Luca Beltrame <luca.beltrame@unifi.it>
 * Copyright (C) 2010 Enrica Calura <enrica.calura@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
