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

package com.miravtech.annotator;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SymbolList extends HashMap<String, Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8767598956815897691L;
	
	
	static private Pattern lineFormat = Pattern.compile("(.*)[ |\\t|,|;](-?[0-9]*\\.?[0-9]*)");
	
	
	public SymbolList(InputStream is) throws Exception {
		Reader isr = new InputStreamReader(is);
		LineNumberReader lnr = new LineNumberReader(isr);
		while (true) {
			String line = lnr.readLine();
			if (line == null)
				break;
			Matcher m = lineFormat.matcher(line);
			if (!m.matches()) {
				System.err.println("Warning, ignored line number: "+lnr.getLineNumber()+" content is:"+line);
				continue;
			}
			String symbol = m.group(1);
			Double value = Double.parseDouble(m.group(2));
			put(symbol, value);
		}
		lnr.close();
		isr.close();
		is.close();
	}
	
	public Collection<Double> getList(Collection<String> s) {
		Set<Double> ret = new HashSet<Double>();
		for (String st: s) 
			if (containsKey(st))
				ret.add(get(st));
		return ret;
	}
}
