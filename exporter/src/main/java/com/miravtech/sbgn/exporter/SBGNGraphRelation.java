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

import com.miravtech.sbgn.ArcType;
import com.miravtech.sbgn.SBGNNodeType;

/**
 * 
 * The relation between two SBGN Nodes
 * 
 */
public class SBGNGraphRelation {
	private SBGNNodeType from;
	private SBGNNodeType to;
	private ArcType reaction;

	public SBGNGraphRelation(SBGNNodeType from, SBGNNodeType to, ArcType reaction) {
		super();
		this.from = from;
		this.to = to;
		this.reaction = reaction;
	}

	public SBGNNodeType getFrom() {
		return from;
	}

	public void setFrom(SBGNNodeType from) {
		this.from = from;
	}

	public SBGNNodeType getTo() {
		return to;
	}

	public void setTo(SBGNNodeType to) {
		this.to = to;
	}

	public ArcType getReaction() {
		return reaction;
	}

	public void setReaction(ArcType reaction) {
		this.reaction = reaction;
	}

}
