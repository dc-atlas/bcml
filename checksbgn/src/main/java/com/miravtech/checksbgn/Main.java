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

package com.miravtech.checksbgn;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBException;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.xml.sax.SAXException;

public class Main {

	static int files = 0;

	/**
	 * @param args
	 * @throws JAXBException
	 * @throws SAXException
	 */
	public static void main(String[] args) throws Exception {

		OptionParser parser = new OptionParser();
		parser.accepts("srcSBGN", "Name of the SBGN file to use.")
				.withRequiredArg().ofType(File.class).describedAs("file path");

		File srcDir = null;
		try {
			OptionSet opts = parser.parse(args);

			srcDir = (File) opts.valueOf("srcSBGN");
			if (srcDir.isDirectory()) {
				for (File f : srcDir.listFiles()) {
					sbgnCheck(f);
				}
			} else {
				sbgnCheck(srcDir);
			}
			System.out.println("Number of files checked: " + files);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception occured: " + e.toString()
					+ "\nPossible commands:\n");
			parser.printHelpOn(System.out);
			return;
		}

	}

	public static void sbgnCheck(File srcSBGN) throws JAXBException,
			SAXException {
		Checker c = new Checker();
		List<CheckReport> r = c.check(srcSBGN);
		if (r.size() != 0) {
			System.out.println("File: " + srcSBGN.getAbsolutePath());
			for (CheckReport cr : r) {
				System.out.println(cr);

			}
		}
		files++;
	}

}
