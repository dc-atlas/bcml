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

package com.miravtech.sbgn.exporter;

public class GeneGeneRel {
	private String from;
	private String to;
	private String reaction;

	public GeneGeneRel(String from, String to, String reaction) {
		super();
		this.from = from;
		this.to = to;
		this.reaction = reaction;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getReaction() {
		return reaction;
	}

	public void setReaction(String reaction) {
		this.reaction = reaction;
	}

}
