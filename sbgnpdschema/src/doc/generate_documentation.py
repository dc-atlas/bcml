#!/usr/bin/env python
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

# -*- coding: utf-8 -*-

import optparse
import os

import libxml2
import libxslt

def generate_documentation(src_file, destination_file, stylesheet_file):

    stylesheet_args = dict()
    style = libxslt.parseStylesheetFile(stylesheet_file)
    document = libxml2.parseFile(src_file)
    result = style.applyStylesheet(document, stylesheet_args)

    fh = open(destination_file, "w")
    style.saveResultToFile(fh, result)
    fh.close()

def main():

    parser = optparse.OptionParser()
    parser.add_option("-o", "--output-file", metavar="FILE",
                      dest="output", help="Save HTML doc to FILE")
    parser.add_option("-s", "--stylesheet-file", metavar="FILE",
                      dest="stylesheet", help="Use FILE as stylesheet")
    options, arguments = parser.parse_args()

    if not options.output:
        parser.error("Output file is required.")

    if not options.stylesheet or not os.path.isfile(options.stylesheet):
        parser.error("Stylesheet file is required")

    if not arguments or not os.path.isfile(arguments[0]):
        parser.error("Source XSD file required")

    source = arguments[0]

    generate_documentation(source, options.output, options.stylesheet)

if __name__ == '__main__':
    main()
