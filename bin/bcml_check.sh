#!/bin/bash
#
#
# Copyright (C) 2010 Razvan Popovici <rp@miravtech.com>
# Copyright (C) 2010 Luca Beltrame <luca.beltrame@unifi.it>
# Copyright (C) 2010 Enrica Calura <enrica.calura@gmail.com>
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Lesser General Public License as published by
# the Free Software Foundation, either version 2.1 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#


JAR_LOCATION="/home/lb_work/Coding/Bioinformatica/sbgn/bin/checksbgn-1.0-SNAPSHOT-jar-with-dependencies.jar"

java -jar $JAR_LOCATION "$@"
