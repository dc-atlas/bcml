@REM
@REM
@REM Copyright (C) 2010 Razvan Popovici <rp@miravtech.com>
@REM Copyright (C) 2010 Luca Beltrame <luca.beltrame@unifi.it>
@REM Copyright (C) 2010 Enrica Calura <enrica.calura@gmail.com>
@REM
@REM This program is free software: you can redistribute it and/or modify
@REM it under the terms of the GNU Lesser General Public License as published by
@REM the Free Software Foundation, either version 2.1 of the License, or
@REM (at your option) any later version.
@REM
@REM This program is distributed in the hope that it will be useful,
@REM but WITHOUT ANY WARRANTY; without even the implied warranty of
@REM MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
@REM GNU Lesser General Public License for more details.
@REM
@REM You should have received a copy of the GNU Lesser General Public License
@REM along with this program.  If not, see <http://www.gnu.org/licenses/>.
@REM

call c:\apache-maven-2.2.1\bin\mvn clean install

cd checksbgn
call c:\apache-maven-2.2.1\bin\mvn assembly:assembly
cd ..

cd graphmlconvert
call c:\apache-maven-2.2.1\bin\mvn assembly:assembly
cd ..


cd exporter
call c:\apache-maven-2.2.1\bin\mvn assembly:assembly
cd ..


cd filter
call c:\apache-maven-2.2.1\bin\mvn assembly:assembly
cd ..

cd annotator
call c:\apache-maven-2.2.1\bin\mvn assembly:assembly
cd ..



copy checksbgn\target\checksbgn-1.0-SNAPSHOT-jar-with-dependencies.jar .
copy graphmlconvert\target\graphmlconvert-1.0-SNAPSHOT-jar-with-dependencies.jar .
copy exporter\target\exporter-1.0-SNAPSHOT-jar-with-dependencies.jar .
copy filter\target\filter-1.0-SNAPSHOT-jar-with-dependencies.jar .
copy annotator\target\annotator-1.0-SNAPSHOT-jar-with-dependencies.jar .

